package com.nft.cn.netty;

import com.nft.cn.netty.handler.LinkLoginHandler;
import com.nft.cn.netty.handler.ModifyHttpRequestHandler;
import com.nft.cn.netty.handler.ModifyHttpResponseHandler;
import com.nft.cn.netty.handler.WriterIdleHandler;
import com.nft.cn.service.*;
import com.nft.cn.util.RedisUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class HttpServer {

    @Value("${server.port}")
    private Integer serverPort;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private UUserAuthNumService uUserAuthNumService;
    @Autowired
    private MintTransferService mintTransferService;

    private static final AttributeKey<HttpVersion> httpVersion = AttributeKey.valueOf("httpVersion");

    @PostConstruct
    @Async
    public void initServer () {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        worker.next().scheduleAtFixedRate(() -> {
            uUserAuthNumService.replaceAuthNumStatus();
        }, 0, 3, TimeUnit.SECONDS);
        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            new ChannelInitializer<NioSocketChannel>() {
                                @Override
                                protected void initChannel(NioSocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(new HttpServerCodec());
                                    ch.pipeline().addLast(new HttpObjectAggregator(65536));
                                    ch.pipeline().addLast(new ModifyHttpRequestHandler());
                                    ch.pipeline().addLast(new ModifyHttpResponseHandler());
                                    ch.pipeline().addLast(new WebSocketServerProtocolHandler("/"));
                                    ch.pipeline().addLast(new LinkLoginHandler(i18nService, uUserService, redisUtils));
                                    ch.pipeline().addLast(new IdleStateHandler(0, 2, 0));
                                    ch.pipeline().addLast(new WriterIdleHandler(i18nService, mintUserService, uUserAuthNumService, mintTransferService));
                                }
                            })
                    .bind(12580);
            channelFuture.sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


}
