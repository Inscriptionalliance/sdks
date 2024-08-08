package com.nft.cn.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nft.cn.entity.UUser;
import com.nft.cn.netty.Session.Session;
import com.nft.cn.netty.Session.SessionFactory;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.RedisUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Slf4j
public class LinkLoginHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    private I18nService i18nService;
    private UUserService uUserService;
    private RedisUtils redisUtils;


    public LinkLoginHandler(){}
    public LinkLoginHandler(I18nService i18nService, UUserService uUserService, RedisUtils redisUtils){
        this.i18nService = i18nService;
        this.uUserService = uUserService;
        this.redisUtils = redisUtils;
    }




    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        JSONObject jsonObject = JSON.parseObject(msg.text());
        Long operate = jsonObject.getLong("operate");
        String lang = jsonObject.getString("lang");
        String token = jsonObject.getString("token");
        UUser tokenUser = uUserService.getTokenUser(token);
        String data = jsonObject.getString("data");
        if (operate == 1) {
            Session session = SessionFactory.getSession();
            session.bind(ctx.channel(), tokenUser.getUserAddress());
        }
        JSONObject jsonObjectResp = JSON.parseObject(msg.text());
        jsonObjectResp.put("code", 20014);
        jsonObjectResp.put("message", i18nService.getMessage("20014"));
        TextWebSocketFrame response = new TextWebSocketFrame(jsonObjectResp.toJSONString());
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        Integer clientProd = ipSocket.getPort();
        JSONObject jsonObjectResp = new JSONObject();
        jsonObjectResp.put("code", 10200);
        jsonObjectResp.put("message", i18nService.getMessage("10200"));
        TextWebSocketFrame response = new TextWebSocketFrame(jsonObjectResp.toJSONString());
        ctx.writeAndFlush(response);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Session session = SessionFactory.getSession();
        session.unbind(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = SessionFactory.getSession();
        session.unbind(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        ctx.channel().close();
        Session session = SessionFactory.getSession();
        session.unbind(ctx.channel());
    }
    
    

}
