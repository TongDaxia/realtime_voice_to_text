package com.tyg.speech.handler;

import com.tyg.speech.config.AppConfig;
import com.tyg.speech.rpc.PythonSpeechClient;
import com.tyg.speech.rpc.PythonSpeechClientPool;
import com.tyg.speech.util.AudioUtils;
import com.tyg.speech.util.LogUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpeechWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final Logger logger = LoggerFactory.getLogger(SpeechWebSocketHandler.class);
    private final AppConfig config;
    private final ExecutorService executor =new ThreadPoolExecutor(
            8, 20, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactory() {
                private final AtomicInteger idx = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "speech-ws-" + idx.getAndIncrement());
                    t.setDaemon(false);
                    return t;
                }
            },
            new ThreadPoolExecutor.AbortPolicy()
    );

    // 每个连接的状态
    private volatile BlockingQueue<byte[]> audioQueue;
    private volatile PythonSpeechClient client;
    private volatile AtomicBoolean started = new AtomicBoolean(false);

    public SpeechWebSocketHandler(AppConfig config) {
        this.config = config;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof BinaryWebSocketFrame) {
            byte[] audio = AudioUtils.convertByteBufToArray(((BinaryWebSocketFrame) frame).content());

            if (started.compareAndSet(false, true)) {
                // 首次收到音频，启动流式识别
                audioQueue = new LinkedBlockingQueue<>();
                executor.submit(() -> {
                    try {
                        client = PythonSpeechClientPool.getClient(
                                config.getPythonRpcHost(), config.getPythonRpcPort());
                        if (client == null) {
                            logger.error("Failed to get client from pool");
                            return;
                        }
                        logger.info("Starting recognizeStreaming for connection: {}", ctx.channel().remoteAddress());
                        client.recognizeStreaming(audioQueue, 16000, resp -> {
                            if (ctx.channel().isActive()) {
                                //回写转写结果
                                ctx.writeAndFlush(new TextWebSocketFrame(resp.getText()));
                            }
                        });
                    } catch (Exception e) {
                        LogUtils.logError("WebSocket gRPC error", e);
                    }
                });
            }

            // 把音频帧加入队列
            if (audioQueue != null) {
                audioQueue.put(audio);
            }

        } else if (frame instanceof CloseWebSocketFrame) {
            logger.info("Received CloseWebSocketFrame, closing connection...");
            if (audioQueue != null) {
                audioQueue.put(new byte[0]); // 空包结束流
            }
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof TooLongFrameException) {
            ctx.writeAndFlush(new CloseWebSocketFrame(1009, "Frame too large"))
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            LogUtils.logError("WebSocket error", cause);
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel inactive, cleaning up...");
        if (audioQueue != null) {
            audioQueue.offer(new byte[0]); // 防止阻塞
        }
        super.channelInactive(ctx);
    }
}