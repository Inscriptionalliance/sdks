package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.FreeTicketRecordMapper;
import com.nft.cn.entity.CreditRecord;
import com.nft.cn.entity.FreeTicketRecord;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.FreeTicketRecordService;
import com.nft.cn.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class FreeTicketRecordServiceImpl extends ServiceImpl<FreeTicketRecordMapper, FreeTicketRecord> implements FreeTicketRecordService {


    @Autowired
    private UUserService uUserService;

    @Override
    public boolean saveFreeTicketRecord(String userAddress, BigDecimal num, Integer type, String relationAddress) {
        UUser byUserAddress = uUserService.getByUserAddress(userAddress);
        FreeTicketRecord record = new FreeTicketRecord();
        if (byUserAddress != null) {
            record.setUserId(byUserAddress.getId());
        }
        record.setUserAddress(userAddress);
        record.setRelationAddress(relationAddress);
        record.setFreeTicket(num);
        record.setType(type);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return save(record);
    }

}
