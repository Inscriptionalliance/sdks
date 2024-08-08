package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.CreditRecordMapper;
import com.nft.cn.entity.CreditRecord;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.CreditRecordService;
import com.nft.cn.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CreditRecordServiceImpl extends ServiceImpl<CreditRecordMapper, CreditRecord> implements CreditRecordService {

    @Autowired
    private UUserService uUserService;

    @Override
    public boolean saveCreditRecord(String userAddress, BigDecimal num, Integer type, String relationAddress) {
        UUser byUserAddress = uUserService.getByUserAddress(userAddress);
        CreditRecord record = new CreditRecord();
        if (byUserAddress != null) {
            record.setUserId(byUserAddress.getId());
        }
        record.setUserAddress(userAddress);
        record.setRelationAddress(relationAddress);
        record.setCredit(num);
        record.setType(type);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return save(record);
    }
}
