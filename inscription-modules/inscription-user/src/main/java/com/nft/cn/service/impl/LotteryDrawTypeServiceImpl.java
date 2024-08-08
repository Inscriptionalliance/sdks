package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.LotteryDrawTypeMapper;
import com.nft.cn.entity.LotteryDrawType;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserBaseInfo;
import com.nft.cn.service.LotteryDrawTypeService;
import com.nft.cn.service.UUserBaseInfoService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.LotteryDrawTypeInfoReq;
import com.nft.cn.vo.resp.LotteryDrawTypeInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LotteryDrawTypeServiceImpl extends ServiceImpl<LotteryDrawTypeMapper, LotteryDrawType> implements LotteryDrawTypeService {

    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private UUserService uUserService;

    @Override
    public BaseResult<LotteryDrawTypeInfoResp> lotteryDrawTypeInfo(LotteryDrawTypeInfoReq lotteryDrawTypeInfoReq) {
        UUser tokenUser = uUserService.getTokenUser();
        LotteryDrawType lotteryDrawType = lambdaQuery().eq(LotteryDrawType::getType, lotteryDrawTypeInfoReq.getType()).one();
        LotteryDrawTypeInfoResp resp = new LotteryDrawTypeInfoResp();
        resp.setType(lotteryDrawType.getType());
        resp.setDepleteNum(lotteryDrawType.getDepleteNum());
        resp.setDepleteUnit(lotteryDrawType.getDepleteUnit());
        resp.setValid(lotteryDrawType.getValid());
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(tokenUser.getUserAddress());
        if (lotteryDrawType.getDepleteUnit() == 1) {
            resp.setUserHoldNum(uUserBaseInfo.getCredit());
        } else if (lotteryDrawType.getDepleteUnit() == 2) {
            resp.setUserHoldNum(uUserBaseInfo.getMintTicket());
        } else if (lotteryDrawType.getDepleteUnit() == 3) {
            resp.setUserHoldNum(uUserBaseInfo.getFreeTicket());
        } else if (lotteryDrawType.getDepleteUnit() == 4) {
            resp.setUserHoldNum(new BigDecimal(uUserBaseInfo.getRarityTicket().toString()));
        } else if (lotteryDrawType.getDepleteUnit() == 5) {
            resp.setUserHoldNum(new BigDecimal(uUserBaseInfo.getEpicTicket().toString()));
        }
        return BaseResult.success(resp);
    }
}
