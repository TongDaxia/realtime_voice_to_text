package com.tyg.speech.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 处理HTTP请求，将WebSocket请求升级
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String websocketPath;

    public HttpRequestHandler(String websocketPath) {
        this.websocketPath = websocketPath;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 如果是WebSocket升级请求，传递给下一个处理器处理
        if (isWebSocketUpgrade(request)) {
            ctx.fireChannelRead(request.retain());
            return;
        }
        
        // 其他HTTP请求返回404
        ctx.close();
    }

    private boolean isWebSocketUpgrade(FullHttpRequest request) {
        return request.headers().contains("Upgrade") && 
               request.headers().get("Upgrade").equalsIgnoreCase("websocket") &&
               request.uri().startsWith(websocketPath);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}