package com.tyg.speech;

import com.tyg.speech.config.AppConfig;
import com.tyg.speech.handler.CorsHandler;
import com.tyg.speech.handler.HttpRequestHandler;
import com.tyg.speech.handler.SpeechWebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpeechServer {
    private final int port;
    private final AppConfig config;

    public SpeechServer(AppConfig config) {
        this.port = config.getServerPort();
        this.config = config;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(4);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) {
                     ChannelPipeline pipeline = ch.pipeline();
                     
                     // HTTP编解码器
                     pipeline.addLast(new HttpServerCodec());
                     pipeline.addLast(new CorsHandler());
                     pipeline.addLast(new HttpObjectAggregator(5*1024*1024));//5M
                     
                     // 认证处理器
//                     pipeline.addLast(new AuthHandler(config));
                     
                     // WebSocket处理器
                     pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                     
                     // 自定义WebSocket处理器
                     pipeline.addLast(new SpeechWebSocketHandler(config));
                     
                     // HTTP请求处理器
                     pipeline.addLast(new HttpRequestHandler("/ws"));
                 }
             });

            Channel ch = b.bind(port).sync().channel();
            log.info("WebSocket server started at port:{} ", port);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        AppConfig config = new AppConfig();
        new SpeechServer(config).run();
    }
}