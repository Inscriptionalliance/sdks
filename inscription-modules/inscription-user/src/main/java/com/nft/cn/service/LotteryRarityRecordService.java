package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.LotteryDrawType;
import com.nft.cn.entity.LotteryRarityRecord;

public interface LotteryRarityRecordService extends IService<LotteryRarityRecord> {

    boolean saveLotteryRarity(String userAddress, LotteryDrawType lotteryDrawType, Long lotteryDrawPrizeId, Integer winLottery);
}
