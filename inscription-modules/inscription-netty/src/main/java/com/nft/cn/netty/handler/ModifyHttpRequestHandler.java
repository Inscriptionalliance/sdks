package com.nft.cn.netty.handler;

import com.nft.cn.netty.Session.Session;
import com.nft.cn.netty.Session.SessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.util.StringUtils;

public class ModifyHttpRequestHandler extends SimpleChannelInboundHandler<HttpRequest> {

    public ModifyHttpRequestHandler () {
        super(HttpRequest.class, false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws Exception {
        String secWebSocketKey = httpRequest.headers().get("Sec-WebSocket-Key");
        if (!StringUtils.isEmpty(secWebSocketKey)) {
            Session session = SessionFactory.getSession();
            session.setWebSocketKey(ctx.channel(), secWebSocketKey);
        }

        ctx.fireChannelRead(httpRequest);
    }
}
