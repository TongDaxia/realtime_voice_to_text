package com.tyg.speech.handler;

import com.tyg.speech.util.LogUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileInputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SpeechServerTest2 {

    private static final int TEST_PORT = 8080;
    private final String TEST_FILE_PATH = "src/test/resources/output.wav";

    @Test
    public void testStreamingRecognition() throws Exception {
        URI uri = new URI("ws://localhost:" + TEST_PORT + "/ws");
        EventLoopGroup group = new NioEventLoopGroup();
        CountDownLatch latch = new CountDownLatch(1);

        DefaultHttpHeaders headers = new DefaultHttpHeaders();
        headers.add("X-Auth-Token", "11223333");

        try {
            WebSocketClientHandshaker hs = WebSocketClientHandshakerFactory
                    .newHandshaker(uri, WebSocketVersion.V13, null, false, headers);

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192));
                            p.addLast(new WebSocketClientHandler(hs));
                            p.addLast(new SimpleChannelInboundHandler<TextWebSocketFrame>(false) {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
                                    String text = msg.text();
                                    log.info("Received Text: {}" , text);

                                    // 等待最终标记
                                    if (text.contains("\"final\":true")) {
                                        LogUtils.info("Final received, closing...");
                                        ctx.writeAndFlush(new CloseWebSocketFrame())
                                                .addListener(ChannelFutureListener.CLOSE);
                                        latch.countDown();
                                    }
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    cause.printStackTrace();
                                    ctx.close();
                                    latch.countDown();
                                }
                            });
                        }
                    });

            Channel ch = b.connect(uri.getHost(), uri.getPort()).sync().channel();
            WebSocketClientHandler handler = (WebSocketClientHandler) ch.pipeline().get(WebSocketClientHandler.class);
            handler.handshakeFuture().sync();

            // ✅ 发送音频
            try (FileInputStream fis = new FileInputStream(TEST_FILE_PATH)) {
                fis.skip(44); // 跳过 WAV header

                byte[] buf = new byte[3200]; //每秒数据量（字节）= 采样率 × 位深度（字节） × 通道数= 1600 Hz × 2字节 × 1 = 3200字节/秒
                int len;
                while ((len = fis.read(buf)) != -1) {
                    byte[] chunk = Arrays.copyOf(buf, len);
                    ch.writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(chunk)));
                    Thread.sleep(1000);
                }
               LogUtils.info("Audio sent, waiting for final...");
            }

            // ✅ 等待最终结果（最多 30 秒）
            boolean ok = latch.await(30, TimeUnit.SECONDS);
            if (!ok) {
                throw new AssertionError("Timeout waiting for final result");
            }

        } finally {
            group.shutdownGracefully();
        }
    }
}