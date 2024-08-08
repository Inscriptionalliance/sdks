package com.nft.cn.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.nft.cn.entity.MintDeploy;
import com.nft.cn.entity.MintUser;
import com.nft.cn.entity.MintUserHold;
import com.nft.cn.entity.UUserAuthNum;
import com.nft.cn.netty.Session.Session;
import com.nft.cn.netty.Session.SessionFactory;
import com.nft.cn.service.*;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.MintMintAuthErrorReq;
import com.nft.cn.vo.req.MintPayAuthErrorReq;
import com.nft.cn.vo.req.MintTransferAuthErrorReq;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class WriterIdleHandler extends ChannelDuplexHandler {

    private I18nService i18nService;
    private MintUserService mintUserService;
    private UUserAuthNumService uUserAuthNumService;
    private MintTransferService mintTransferService;

    public WriterIdleHandler(){}
    public WriterIdleHandler(I18nService i18nService, MintUserService mintUserService, UUserAuthNumService uUserAuthNumService, MintTransferService mintTransferService){
        this.i18nService = i18nService;
        this.mintUserService = mintUserService;
        this.uUserAuthNumService = uUserAuthNumService;
        this.mintTransferService = mintTransferService;
    }



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Session session = SessionFactory.getSession();
            String userAddress = session.getUserAddress(ctx.channel());
            if (StringUtils.isEmpty(userAddress)) {
                ctx.channel().close();
                session.unbind(ctx.channel());
                return;
            }
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                if (sendAuthNumStatus(ctx, session, userAddress)) return;
                sendMintStatus(ctx, session, userAddress);
            }
        }
    }

    private boolean sendAuthNumStatus(ChannelHandlerContext ctx, Session session, String userAddress) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operate", 2);
        jsonObject.put("code", 4);
        jsonObject.put("message", i18nService.getMessage("20215"));
        UUserAuthNum uUserAuthNum = session.getUUserAuthNum(userAddress);
        LocalDateTime now = LocalDateTime.now();
        if (uUserAuthNum != null) {
            if (uUserAuthNum.getPayBoxAuthNumStatus() == 1){
                jsonObject.put("type", 1);
                LocalDateTime authNumExpire = uUserAuthNum.getPayBoxAuthNumExpire();
                if (now.compareTo(authNumExpire) > 0) {
                    String authNum = uUserAuthNum.getPayBoxAuthNum();
                    MintPayAuthErrorReq req = new MintPayAuthErrorReq();
                    req.setAuthNum(authNum);
                    uUserAuthNumService.updateAuthNumStatus(userAddress, 4, 1);
                    TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                    ctx.writeAndFlush(response);
                    return true;
                }
            }
            if (uUserAuthNum.getMintAuthNumStatus() == 1) {
                jsonObject.put("type", 2);
                LocalDateTime authNumExpire = uUserAuthNum.getMintAuthNumExpire();
                if (now.compareTo(authNumExpire) > 0) {
                    String authNum = uUserAuthNum.getMintAuthNum();
                    MintMintAuthErrorReq req = new MintMintAuthErrorReq();
                    req.setAuthNum(authNum);
                    uUserAuthNumService.updateAuthNumStatus(userAddress, 4, 2);
                    TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                    ctx.writeAndFlush(response);
                    return true;
                }
            }
            if (uUserAuthNum.getMintTransferAuthNumStatus() == 1) {
                jsonObject.put("type", 3);
                LocalDateTime authNumExpire = uUserAuthNum.getMintTransferAuthNumExpire();
                if (now.compareTo(authNumExpire) > 0) {
                    String authNum = uUserAuthNum.getMintTransferAuthNum();
                    MintTransferAuthErrorReq req = new MintTransferAuthErrorReq();
                    req.setAuthNum(authNum);
                    uUserAuthNumService.updateAuthNumStatus(userAddress, 4, 3);
                    TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                    ctx.writeAndFlush(response);
                    return true;
                }
            }

        }
        return false;
    }

    private void sendMintStatus(ChannelHandlerContext ctx, Session session, String userAddress) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operate", 1);
        jsonObject.put("type", 1);
        MintUserHold mintUserHold = session.getMintUserHold(userAddress);
        if (mintUserHold == null) {
            jsonObject.put("code", 20096);
            jsonObject.put("message", i18nService.getMessage("20096"));
            TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
            ctx.writeAndFlush(response);
        } else {
            if (mintUserHold.getMintStatus() == 0) {
                jsonObject.put("code", 0);
                jsonObject.put("message", i18nService.getMessage("20300"));
                TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                ctx.writeAndFlush(response);
            } else if (mintUserHold.getMintStatus() == 1) {
                jsonObject.put("code", 1);
                jsonObject.put("message", i18nService.getMessage("20301"));
                TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                ctx.writeAndFlush(response);
            } else if (mintUserHold.getMintStatus() == 2) {
                MintUser mintUser = session.getMintUser(userAddress);
                if (mintUser == null) {
                    jsonObject.put("code", 20096);
                    jsonObject.put("message", i18nService.getMessage("20096"));
                    TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                    ctx.writeAndFlush(response);
                    return;
                }
                jsonObject.put("code", 2);
                jsonObject.put("message", i18nService.getMessage("20302"));
                TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                ctx.writeAndFlush(response);
            } else if (mintUserHold.getMintStatus() == 3) {
                jsonObject.put("code", 3);
                jsonObject.put("message", i18nService.getMessage("20303"));
                TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                ctx.writeAndFlush(response);
            } else if (mintUserHold.getMintStatus() == -1) {
                jsonObject.put("code", -1);
                jsonObject.put("message", i18nService.getMessage("20219"));
                TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                ctx.writeAndFlush(response);
            } else {
                jsonObject.put("code", 0);
                jsonObject.put("message", i18nService.getMessage("20300"));
                TextWebSocketFrame response = new TextWebSocketFrame(jsonObject.toJSONString());
                ctx.writeAndFlush(response);
            }
        }
    }


}
