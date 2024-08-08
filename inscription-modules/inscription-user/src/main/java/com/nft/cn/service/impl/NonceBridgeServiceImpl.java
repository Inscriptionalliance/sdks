package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.NonceBridgeMapper;
import com.nft.cn.entity.NonceBridge;
import com.nft.cn.entity.NonceMarket;
import com.nft.cn.service.NonceBridgeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NonceBridgeServiceImpl extends ServiceImpl<NonceBridgeMapper, NonceBridge> implements NonceBridgeService {


    @Override
    public NonceBridge getByUserAddress(String userAddress) {
        NonceBridge one = lambdaQuery().eq(NonceBridge::getUserAddress, userAddress).one();
        if (one == null) {
            one = new NonceBridge();
            one.setUserAddress(userAddress);
            one.setNonce(0);
            one.setUpdateTime(LocalDateTime.now());
            one.setCreateTime(LocalDateTime.now());
            save(one);
        }
        return one;
    }


}
