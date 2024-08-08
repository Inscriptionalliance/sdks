package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintTicketRecord;

import java.math.BigDecimal;
import java.util.List;

public interface MintTicketRecordService extends IService<MintTicketRecord> {

    boolean saveMintTicketRecord(String userAddress, BigDecimal num, Integer type, String relationAddress);


}
