package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintCommunityAdd;

import java.math.BigDecimal;

public interface MintCommunityAddService extends IService<MintCommunityAdd> {

    void saveCommunityAddRunnable(Long userId, String userAddress, BigDecimal mintNum, BigDecimal paidMintNum);

}
