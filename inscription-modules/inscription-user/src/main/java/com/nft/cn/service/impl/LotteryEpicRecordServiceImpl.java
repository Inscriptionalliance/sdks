package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.LotteryEpicRecordMapper;
import com.nft.cn.entity.LotteryCommonRecord;
import com.nft.cn.entity.LotteryDrawType;
import com.nft.cn.entity.LotteryEpicRecord;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.LotteryEpicRecordService;
import com.nft.cn.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LotteryEpicRecordServiceImpl extends ServiceImpl<LotteryEpicRecordMapper, LotteryEpicRecord> implements LotteryEpicRecordService {

    @Autowired
    private UUserService uUserService;

    @Override
    public boolean saveLotteryEpic(String userAddress, LotteryDrawType lotteryDrawType, Long lotteryDrawPrizeId, Integer winLottery) {
        UUser byUserAddress = uUserService.getByUserAddress(userAddress);
        LotteryEpicRecord record = new LotteryEpicRecord();
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
