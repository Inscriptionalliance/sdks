package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.CreditRecord;

import java.math.BigDecimal;

public interface CreditRecordService extends IService<CreditRecord> {

    boolean saveCreditRecord(String userAddress, BigDecimal num, Integer type, String relationAddress);
}
