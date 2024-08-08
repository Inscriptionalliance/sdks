package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.FreeTicketRecord;

import java.math.BigDecimal;

public interface FreeTicketRecordService extends IService<FreeTicketRecord> {

    boolean saveFreeTicketRecord(String userAddress, BigDecimal num, Integer type, String relationAddress);

}
