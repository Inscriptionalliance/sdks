package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.LotteryDrawPrize;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.LotteryDrawReq;
import com.nft.cn.vo.resp.LotteryDrawResp;

public interface LotteryDrawPrizeService extends IService<LotteryDrawPrize> {


    BaseResult<LotteryDrawResp> lotteryDraw(LotteryDrawReq lotteryDrawReq);

}
