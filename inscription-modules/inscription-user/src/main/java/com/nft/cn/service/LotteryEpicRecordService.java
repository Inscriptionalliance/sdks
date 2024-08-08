package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.LotteryDrawType;
import com.nft.cn.entity.LotteryEpicRecord;

public interface LotteryEpicRecordService extends IService<LotteryEpicRecord> {

    boolean saveLotteryEpic(String userAddress, LotteryDrawType lotteryDrawType, Long lotteryDrawPrizeId, Integer winLottery);
}
