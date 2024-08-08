package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.BridgeHoldBinanceBrc20Btia;

import java.math.BigDecimal;

public interface BridgeHoldBinanceBrc20BtiaService extends IService<BridgeHoldBinanceBrc20Btia> {

    BridgeHoldBinanceBrc20Btia getByUserAddress(String userAddress);

    boolean updateBalance(String userAddress, BigDecimal amount, Integer operate);
}
