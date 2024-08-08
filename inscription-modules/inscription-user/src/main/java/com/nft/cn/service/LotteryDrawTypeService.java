package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.LotteryDrawType;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.LotteryDrawTypeInfoReq;
import com.nft.cn.vo.resp.LotteryDrawTypeInfoResp;

public interface LotteryDrawTypeService extends IService<LotteryDrawType> {

    BaseResult<LotteryDrawTypeInfoResp> lotteryDrawTypeInfo(LotteryDrawTypeInfoReq lotteryDrawTypeInfoReq);

}
