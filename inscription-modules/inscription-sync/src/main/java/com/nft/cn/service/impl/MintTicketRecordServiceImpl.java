package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintTicketRecordMapper;
import com.nft.cn.entity.MintTicketRecord;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.MintTicketRecordService;
import com.nft.cn.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MintTicketRecordServiceImpl extends ServiceImpl<MintTicketRecordMapper, MintTicketRecord> implements MintTicketRecordService {

    @Autowired
    private UUserService uUserService;

    @Override
    public boolean saveMintTicketRecord(String userAddress, BigDecimal num, Integer type, String relationAddress) {
        if (num.compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        UUser byUserAddress = uUserService.getByUserAddress(userAddress);
        MintTicketRecord record = new MintTicketRecord();
        if (byUserAddress != null) {
            record.setUserId(byUserAddress.getId());
        }
        record.setUserAddress(userAddress);
        record.setRelationAddress(relationAddress);
        record.setMintTicket(num);
        record.setType(type);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return save(record);
    }
}
