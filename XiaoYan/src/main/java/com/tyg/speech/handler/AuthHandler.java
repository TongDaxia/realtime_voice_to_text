package com.tyg.speech.handler;

import com.tyg.speech.config.AppConfig;
import com.tyg.speech.util.LogUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.ReferenceCountUtil;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    private final AppConfig config;

    public AuthHandler(AppConfig config) {
        this.config = config;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            HttpHeaders headers = request.headers();
            
            String authToken = headers.get("X-Auth-Token");
            if (config.getAuthToken().equals(authToken)) {
                // 认证通过，继续处理
                LogUtils.logAuthSuccess(ctx.channel().remoteAddress().toString());
                super.channelRead(ctx, msg);
            } else {
                // 认证失败，关闭连接
                LogUtils.logAuthFailure(ctx.channel().remoteAddress().toString());
                ReferenceCountUtil.release(msg);
                ctx.close();
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LogUtils.logError("Auth error: " + cause.getMessage());
        ctx.close();
    }
}