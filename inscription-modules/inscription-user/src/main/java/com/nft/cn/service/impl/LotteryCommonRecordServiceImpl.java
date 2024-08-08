package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.LotteryCommonRecordMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.LotteryCommonRecordService;
import com.nft.cn.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LotteryCommonRecordServiceImpl extends ServiceImpl<LotteryCommonRecordMapper, LotteryCommonRecord> implements LotteryCommonRecordService {

    @Autowired
    private UUserService uUserService;

    @Override
    public boolean saveLotteryCommon(String userAddress, LotteryDrawType lotteryDrawType, Long lotteryDrawPrizeId, Integer winLottery) {
        UUser byUserAddress = uUserService.getByUserAddress(userAddress);
        LotteryCommonRecord record = new LotteryCommonRecord();
        if (byUserAddress != null) {
            record.setUserId(byUserAddress.getId());
        }
        record.setUserAddress(userAddress);
        record.setDepleteNum(lotteryDrawType.getDepleteNum());
        record.setDepleteUnit(lotteryDrawType.getDepleteUnit());
        record.setWinLottery(winLottery);
        record.setPrizeId(lotteryDrawPrizeId);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return save(record);
    }
}
