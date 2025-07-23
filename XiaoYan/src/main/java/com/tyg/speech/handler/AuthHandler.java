package com.tyg.speech.handler;

import com.tyg.speech.config.AppConfig;
import com.tyg.speech.util.LogUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            String uri = request.getUri();
            log.info("收到完整请求，uri:{}", uri);
//            log.info("收到完整请求，HTTP头:{}", headers);

            String authToken = headers.get("X-Auth-Token");
            if (config.getAuthToken().equals(authToken)) {
                // 认证通过，继续处理
                LogUtils.logAuthSuccess(ctx.channel().remoteAddress().toString());
                super.channelRead(ctx, msg);
            } else if (uri.contains(config.getAuthToken())) {
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