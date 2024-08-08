package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.NonceBridge;

public interface NonceBridgeService extends IService<NonceBridge> {

    NonceBridge getByUserAddress(String userAddress);

    void updateNonce(String userAddress);
}
