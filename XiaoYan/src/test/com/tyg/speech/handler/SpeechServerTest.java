package com.tyg.speech.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
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
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class SpeechServerTest {
    private static final Logger logger = LoggerFactory.getLogger(SpeechServerTest.class);

    private static final int TARGET_SAMPLE_RATE = 16000; // 目标采样率(Hz)
    private static final int BITS_PER_SAMPLE = 16; // 位深度
    private static final int TARGET_CHANNELS = 1; // 单声道
    private static final int DESIRED_CHUNK_DURATION_MS = 500; // 每次发送 DESIRED_CHUNK_DURATION_MS 的音频数据

    private final int TEST_PORT = 8080;
    private final String TEST_FILE_PATH = "src/test/resources/output.wav";

    // 计算每次应该读取的字节数
    private int calculateChunkSize() {
        // 公式: (采样率 * 时长 * 位深度 * 通道数) / (8 * 1000)
        return (TARGET_SAMPLE_RATE * DESIRED_CHUNK_DURATION_MS * BITS_PER_SAMPLE * TARGET_CHANNELS) / (8 * 1000);
    }

    @Test
    public void testSpeechRecognition() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            URI uri = new URI("ws://localhost:" + TEST_PORT + "/ws");
            DefaultHttpHeaders headers = new DefaultHttpHeaders();
            headers.add("X-Auth-Token", "11223333");

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpClientCodec());
                            p.addLast(new HttpObjectAggregator(8192));

                            WebSocketClientHandler handler = new WebSocketClientHandler(
                                    WebSocketClientHandshakerFactory.newHandshaker(
                                            uri, WebSocketVersion.V13, null, false, headers));

                            p.addLast(handler);
                            p.addLast(new SimpleChannelInboundHandler<TextWebSocketFrame>(false) {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
                                    logger.info("Received text: {}", msg.text());
                                    latch.countDown();
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

            File audioFile = new File(TEST_FILE_PATH);
            if (!audioFile.exists()) {
                throw new RuntimeException("Test audio file not found: " + TEST_FILE_PATH);
            }

            AudioInputStream originalStream = AudioSystem.getAudioInputStream(audioFile);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = originalStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] originalAudioData = baos.toByteArray();
            originalStream.close();

            // 分块发送转换后的音频
            int chunkSize = calculateChunkSize();
            int totalBytes = originalAudioData.length;
            int offset = 0;

            while (offset < totalBytes) {
                int remaining = totalBytes - offset;
                int currentChunkSize = Math.min(chunkSize, remaining);

                byte[] chunk = new byte[currentChunkSize];
                System.arraycopy(originalAudioData, offset, chunk, 0, currentChunkSize);
                sendAudioChunk(ch, chunk);

                offset += currentChunkSize;
                Thread.sleep(DESIRED_CHUNK_DURATION_MS);
            }

            ch.writeAndFlush(new CloseWebSocketFrame());
            assertTrue(latch.await(60, TimeUnit.SECONDS));

        } finally {
            group.shutdownGracefully();
        }
    }

    private void sendAudioChunk(Channel ch, byte[] audioData) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(audioData);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(byteBuffer);
        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
        ch.writeAndFlush(frame);

        // 计算实际发送的音频时长（毫秒）
        double actualDuration = (double) audioData.length * 8 * 1000 /
                (BITS_PER_SAMPLE * TARGET_CHANNELS * TARGET_SAMPLE_RATE);
        logger.info("Sent {}ms of audio data.", actualDuration);
    }



}