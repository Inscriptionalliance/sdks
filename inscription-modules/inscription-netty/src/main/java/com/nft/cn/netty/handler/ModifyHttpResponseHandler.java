package com.nft.cn.netty.handler;

import com.nft.cn.netty.Session.Session;
import com.nft.cn.netty.Session.SessionFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpResponse;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

public class ModifyHttpResponseHandler extends SimpleChannelInboundHandler<HttpResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpResponse httpResponse ) throws Exception {
        Session session = SessionFactory.getSession();
        String webSocketKey = session.getWebSocketKey(ctx.channel());
        ctx.fireChannelRead(httpResponse);
    }
}
