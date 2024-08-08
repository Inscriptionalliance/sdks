package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintSwap;
import com.nft.cn.vo.DataSyncVO;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface MintSwapService extends IService<MintSwap> {

    void swap(DataSyncVO dataSyncVO, String hash, Long blockNum, String swapContractAddress);

    BigDecimal getPrice(BigInteger mintNum, Integer type);
}
