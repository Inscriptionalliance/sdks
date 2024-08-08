package com.nft.cn.netty.Session;

import com.nft.cn.entity.MintUser;
import com.nft.cn.entity.MintUserHold;
import com.nft.cn.entity.UUserAuthNum;
import io.netty.channel.Channel;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SessionMemoryImpl implements Session{

    private final Map<String, List<Channel>> userAddressChannelMap = new ConcurrentHashMap<>();
    private final Map<Channel, String> channelUserAddressMap = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> userAddressAttributesMap = new ConcurrentHashMap<>();
    private final Map<Channel, Map<String, Object>> channelAttributesMap = new ConcurrentHashMap<>();
    private final Map<Channel, String> webSocketKeyMap = new ConcurrentHashMap<>();

    private List<MintUserHold> mintUserHoldList = new ArrayList<>();
    private List<MintUser> mintUserList = new ArrayList<>();
    private List<UUserAuthNum> uUserAuthNumList = new ArrayList<>();

    @Override
    public void bind(Channel channel, String userAddress) {
        List<Channel> channels = userAddressChannelMap.get(userAddress);
        if (CollectionUtils.isEmpty(channels)) {
            channels = new ArrayList<>();
            channels.add(channel);
            userAddressChannelMap.put(userAddress, channels);
        } else {
            if (channels.size() > 4) {
                channels = new ArrayList<>();
            }
            channels.add(channel);
        }
        channelUserAddressMap.put(channel, userAddress);
        userAddressAttributesMap.put(userAddress, new ConcurrentHashMap<>());
        channelAttributesMap.put(channel, new ConcurrentHashMap<>());
    }

    @Override
    public void unbind(Channel channel) {
        String userAddress = channelUserAddressMap.remove(channel);
        channelAttributesMap.remove(channel);
        webSocketKeyMap.remove(channel);
        if (!StringUtils.isEmpty(userAddress)) {
            List<Channel> channels = userAddressChannelMap.get(userAddress);
            channels.remove(channel);
            userAddressAttributesMap.remove(userAddress);
        }
    }

    @Override
    public Object getAttribute(Channel channel, String paramName) {
        return channelAttributesMap.get(channel).get(paramName);
    }

    @Override
    public Object getAttribute(String userAddress, String paramName) {
        return userAddressAttributesMap.get(userAddress).get(paramName);
    }

    @Override
    public void setAttribute(Channel channel, String paramName, Object value) {
        String userAddress = channelUserAddressMap.get(channel);
        if (!StringUtils.isEmpty(userAddress)) {
            Map<String, Object> channelParamMap = channelAttributesMap.get(channel);
            channelParamMap.put(paramName, value);
            Map<String, Object> userAddressParamMap = userAddressAttributesMap.get(userAddress);
            userAddressParamMap.put(paramName, value);
        }
    }

    @Override
    public void setAttribute(String userAddress, String paramName, Object value) {
        List<Channel> channels = userAddressChannelMap.get(userAddress);
        if (!CollectionUtils.isEmpty(channels)) {
            for (Channel channel : channels) {
                Map<String, Object> userAddressParamMap = userAddressAttributesMap.get(userAddress);
                userAddressParamMap.put(paramName, value);
                Map<String, Object> channelParamMap = channelAttributesMap.get(channel);
                channelParamMap.put(paramName, value);
            }
        }
    }

    @Override
    public List<Channel> getChannel(String userAddress) {
        return userAddressChannelMap.get(userAddress);
    }

    @Override
    public String getUserAddress(Channel channel) {
        return channelUserAddressMap.get(channel);
    }

    @Override
    public void setWebSocketKey(Channel channel, String key) {
        webSocketKeyMap.put(channel, key);
    }

    @Override
    public String getWebSocketKey(Channel channel) {
        return webSocketKeyMap.get(channel);
    }

    @Override
    public List<String> getUserAddressList() {
        return new ArrayList<>(userAddressChannelMap.keySet());
    }

    @Override
    public void setMintUserHoldList(List<MintUserHold> mintUserHoldList) {
        this.mintUserHoldList = mintUserHoldList;
    }

    @Override
    public void setMintUserList(List<MintUser> mintUserList) {
        this.mintUserList = mintUserList;
    }

    @Override
    public void setUUserAuthNumList(List<UUserAuthNum> uUserAuthNumList) {
        this.uUserAuthNumList = uUserAuthNumList;
    }

    @Override
    public MintUserHold getMintUserHold(String userAddress) {
        List<MintUserHold> collect = mintUserHoldList.stream().filter(mintUserHold -> mintUserHold.getUserAddress().equalsIgnoreCase(userAddress)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            return collect.get(0);
        }
        return null;
    }

    @Override
    public MintUser getMintUser(String userAddress) {
        List<MintUser> collect = mintUserList.stream().filter(mintUser -> mintUser.getUserAddress().equalsIgnoreCase(userAddress)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            return collect.get(0);
        }
        return null;
    }

    @Override
    public UUserAuthNum getUUserAuthNum(String userAddress) {
        List<UUserAuthNum> collect = uUserAuthNumList.stream().filter(uUserAuthNum -> uUserAuthNum.getUserAddress().equalsIgnoreCase(userAddress)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            return collect.get(0);
        }
        return null;
    }
}
