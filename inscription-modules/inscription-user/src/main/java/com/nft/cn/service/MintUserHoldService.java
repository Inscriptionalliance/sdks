package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintUserHold;

import java.math.BigDecimal;

public interface MintUserHoldService extends IService<MintUserHold> {

    MintUserHold getMintUserHold(String userAddress, Long deployId);

    boolean updateMintNum(String userAddress, Long deployId, BigDecimal mintNum, Integer operate);

    boolean updateUsdaNum(String userAddress, Long deployId, BigDecimal usdaNum, Integer operate);
}
