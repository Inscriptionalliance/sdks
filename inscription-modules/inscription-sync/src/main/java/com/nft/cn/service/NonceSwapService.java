package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.NonceSwap;

public interface NonceSwapService extends IService<NonceSwap> {

    NonceSwap getByUserAddress(String userAddress);

    void updateNonce(String userAddress);
}
