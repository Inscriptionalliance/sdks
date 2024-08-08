package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.NonceMarketMapper;
import com.nft.cn.entity.NonceMarket;
import com.nft.cn.entity.NonceSwap;
import com.nft.cn.service.NonceMarketService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NonceMarketServiceImpl extends ServiceImpl<NonceMarketMapper, NonceMarket> implements NonceMarketService {


    @Override
    public NonceMarket getByUserAddress(String userAddress) {
        NonceMarket one = lambdaQuery().eq(NonceMarket::getUserAddress, userAddress).one();
        if (one == null) {
            one = new NonceMarket();
            one.setUserAddress(userAddress);
            one.setNonce(0);
            one.setUpdateTime(LocalDateTime.now());
            one.setCreateTime(LocalDateTime.now());
            save(one);
        }
        return one;
    }

}
