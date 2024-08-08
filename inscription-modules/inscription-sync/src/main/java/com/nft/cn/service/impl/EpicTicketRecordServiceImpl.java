package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.EpicTicketRecordMapper;
import com.nft.cn.entity.EpicTicketRecord;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.EpicTicketRecordService;
import com.nft.cn.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EpicTicketRecordServiceImpl extends ServiceImpl<EpicTicketRecordMapper, EpicTicketRecord> implements EpicTicketRecordService {

    @Autowired
    private UUserService uUserService;

    @Override
    public boolean saveEpicTicket(String userAddress, Long epicTicketNum, Integer type, String relationAddress) {
        UUser byUserAddress = uUserService.getByUserAddress(userAddress);
        EpicTicketRecord record = new EpicTicketRecord();
        if (byUserAddress != null) {
            record.setUserId(byUserAddress.getId());
        }
        record.setUserAddress(userAddress);
        record.setRelationAddress(relationAddress);
        record.setEpicTicket(epicTicketNum);
        record.setType(type);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return save(record);
    }
}
