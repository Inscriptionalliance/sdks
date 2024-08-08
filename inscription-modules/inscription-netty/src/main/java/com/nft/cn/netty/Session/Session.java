package com.nft.cn.netty.Session;

import com.nft.cn.entity.MintUser;
import com.nft.cn.entity.MintUserHold;
import com.nft.cn.entity.UUserAuthNum;
import io.netty.channel.Channel;

import java.util.List;

public interface Session {

    void bind(Channel channel, String userAddress);

    void unbind(Channel channel);

    Object getAttribute(Channel channel, String paramName);

    Object getAttribute(String userAddress, String paramName);

    void setAttribute(Channel channel, String paramName, Object value);

    void setAttribute(String userAddress, String paramName, Object value);

    List<Channel> getChannel(String userAddress);

    String getUserAddress(Channel channel);

    void setWebSocketKey(Channel channel, String key);

    String getWebSocketKey(Channel channel);

    List<String> getUserAddressList();

    void setMintUserHoldList(List<MintUserHold> mintUserHoldList);

    void setMintUserList(List<MintUser> mintUserList);

    void setUUserAuthNumList(List<UUserAuthNum> uUserAuthNumList);

    MintUserHold getMintUserHold(String userAddress);

    MintUser getMintUser(String userAddress);

    UUserAuthNum getUUserAuthNum(String userAddress);



}
