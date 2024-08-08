package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.NonceMarket;

public interface NonceMarketService extends IService<NonceMarket> {

    NonceMarket getByUserAddress(String userAddress);

    void updateNonce(String userAddress);
}
