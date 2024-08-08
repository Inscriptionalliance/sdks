package com.nft.cn.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.concurrent.atomic.AtomicLong;

public class SpeedMonitorHandler extends ChannelDuplexHandler {

    private final AtomicLong totalReadBytes = new AtomicLong(0);
    private final AtomicLong totalWriteBytes = new AtomicLong(0);
    private long lastReadTime = System.currentTimeMillis();
    private long lastWriteTime = System.currentTimeMillis();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {


        int bytesRead = ((ByteBuf) msg).readableBytes();
        totalReadBytes.addAndGet(bytesRead);
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - lastReadTime;
        if (duration >= 1000) {
            long speed = (totalReadBytes.getAndSet(0) * 1000) / 1024 / duration;
            System.out.println("Read Speed: " + speed + " kb/s");
            lastReadTime = currentTime;
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        int bytesWrite = ((ByteBuf) msg).readableBytes();
        totalWriteBytes.addAndGet(bytesWrite);
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - lastWriteTime;
        if (duration >= 1000) {
            long speed = (totalWriteBytes.getAndSet(0) * 1000) / 1024 / duration;
            System.out.println("Write Speed: " + speed + " kb/s");
            lastWriteTime = currentTime;
        }
        ctx.write(msg, promise);
    }



}
