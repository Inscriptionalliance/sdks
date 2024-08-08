package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.RarityTicketRecordMapper;
import com.nft.cn.entity.RarityTicketRecord;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.RarityTicketRecordService;
import com.nft.cn.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RarityTicketRecordServiceImpl extends ServiceImpl<RarityTicketRecordMapper, RarityTicketRecord> implements RarityTicketRecordService {

    @Autowired
    private UUserService uUserService;

    @Override
    public boolean saveRarityTicket(String userAddress, Long rarityTicketNum, Integer type, String relationAddress) {
        UUser byUserAddress = uUserService.getByUserAddress(userAddress);
        RarityTicketRecord record = new RarityTicketRecord();
        if (byUserAddress != null) {
            record.setUserId(byUserAddress.getId());
        }
        record.setUserAddress(userAddress);
        record.setRelationAddress(relationAddress);
        record.setRarityTicket(rarityTicketNum);
        record.setType(type);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return save(record);
    }
}
