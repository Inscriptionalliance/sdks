package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.LotteryCommonRecord;
import com.nft.cn.entity.LotteryDrawType;

public interface LotteryCommonRecordService extends IService<LotteryCommonRecord> {

    boolean saveLotteryCommon(String userAddress, LotteryDrawType lotteryDrawType, Long lotteryDrawPrizeId, Integer winLottery);
}
