package com.nft.cn.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class WebSocketTrafficHandler extends ChannelDuplexHandler {

    private final AtomicLong lastReadBytes = new AtomicLong();
    private final AtomicLong lastWriteBytes = new AtomicLong();
    private final AtomicReference<String> ctxId = new AtomicReference();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf buf = (ByteBuf) msg;
            lastReadBytes.addAndGet(buf.readableBytes());
            ctxId.getAndSet(ctx.channel().id().asShortText());
            calculateSpeed();
        } finally {
            ReferenceCountUtil.release(msg);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            ByteBuf buf = (ByteBuf) msg;
            lastWriteBytes.addAndGet(buf.readableBytes());
            calculateSpeed();
        } finally {
            ReferenceCountUtil.release(msg);
        }
        super.write(ctx, msg, promise);
    }

    public void calculateSpeed() {
        long readBytes = lastReadBytes.getAndSet(0);
        long writeBytes = lastWriteBytes.getAndSet(0);
        long uploadSpeed = readBytes;
        long downloadSpeed = writeBytes;
    }

}
