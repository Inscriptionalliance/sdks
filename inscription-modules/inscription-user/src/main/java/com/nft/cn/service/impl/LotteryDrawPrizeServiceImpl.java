package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.LotteryDrawPrizeMapper;
import com.nft.cn.entity.LotteryDrawPrize;
import com.nft.cn.entity.LotteryDrawType;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserBaseInfo;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.LotteryDrawReq;
import com.nft.cn.vo.resp.LotteryDrawResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class LotteryDrawPrizeServiceImpl extends ServiceImpl<LotteryDrawPrizeMapper, LotteryDrawPrize> implements LotteryDrawPrizeService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private LotteryDrawTypeService lotteryDrawTypeService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private LotteryCommonRecordService lotteryCommonRecordService;
    @Autowired
    private LotteryRarityRecordService lotteryRarityRecordService;
    @Autowired
    private LotteryEpicRecordService lotteryEpicRecordService;

    @Override
    public BaseResult<LotteryDrawResp> lotteryDraw(LotteryDrawReq lotteryDrawReq) {
        UUser tokenUser = uUserService.getTokenUser();
        LotteryDrawType lotteryDrawType = lotteryDrawTypeService.lambdaQuery().eq(LotteryDrawType::getType, lotteryDrawReq.getType()).one();
        if (lotteryDrawType == null || lotteryDrawType.getValid() == 0) {
            return BaseResult.fail(i18nService.getMessage("20050"));
        }
        BigDecimal depleteNum = lotteryDrawType.getDepleteNum();
        BigDecimal userHoldNum = BigDecimal.ZERO;
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(tokenUser.getUserAddress());
        if (lotteryDrawType.getDepleteUnit() == 1) {
            userHoldNum = uUserBaseInfo.getCredit();
        } else if (lotteryDrawType.getDepleteUnit() == 2) {
            userHoldNum = uUserBaseInfo.getMintTicket();
        } else if (lotteryDrawType.getDepleteUnit() == 3) {
            userHoldNum = uUserBaseInfo.getFreeTicket();
        } else if (lotteryDrawType.getDepleteUnit() == 4) {
            userHoldNum = new BigDecimal(uUserBaseInfo.getRarityTicket().toString());
        } else if (lotteryDrawType.getDepleteUnit() == 5) {
            userHoldNum = new BigDecimal(uUserBaseInfo.getEpicTicket().toString());
        }
        if (userHoldNum.compareTo(BigDecimal.ZERO) <= 0 || userHoldNum.compareTo(depleteNum) < 0) {
            return BaseResult.fail(i18nService.getMessage("20051"));
        }
        int incomeType = 200;
        int depleteType = 200;
        if (lotteryDrawType.getType() == 1) {
            incomeType = 111;
            depleteType = 205;
        } else if (lotteryDrawType.getType() == 2) {
            incomeType = 112;
            depleteType = 206;
        } else if (lotteryDrawType.getType() == 3) {
            incomeType = 113;
            depleteType = 207;
        }
        boolean falg = false;
        if (lotteryDrawType.getDepleteUnit() == 1) {
            falg = uUserBaseInfoService.updateCredit(tokenUser.getUserAddress(), depleteNum, depleteType, null);
        } else if (lotteryDrawType.getDepleteUnit() == 2) {
            falg = uUserBaseInfoService.updateMintTicket(tokenUser.getUserAddress(), depleteNum, depleteType, null);
        } else if (lotteryDrawType.getDepleteUnit() == 3) {
            falg = uUserBaseInfoService.updateFreeTicket(tokenUser.getUserAddress(), depleteNum, depleteType, null);
        } else if (lotteryDrawType.getDepleteUnit() == 4) {
            falg = uUserBaseInfoService.updateRarityTicket(tokenUser.getUserAddress(), depleteNum.longValue(), depleteType, null);
        } else if (lotteryDrawType.getDepleteUnit() == 5) {
            falg = uUserBaseInfoService.updateEpicTicket(tokenUser.getUserAddress(), depleteNum.longValue(), depleteType, null);
        }
        if (!falg) {
            return BaseResult.fail(i18nService.getMessage("20051"));
        }
        List<LotteryDrawPrize> lotteryDrawPrizeList = lambdaQuery().eq(LotteryDrawPrize::getType, lotteryDrawReq.getType()).list();
        if (CollectionUtils.isEmpty(lotteryDrawPrizeList)) {
            return BaseResult.fail(i18nService.getMessage("20052"));
        }
        long weightAll = lotteryDrawPrizeList.stream().mapToLong(LotteryDrawPrize::getWeight).sum();
        int weightHeight = (int) (weightAll >> 32);
        int weightLow = (int) (weightAll % (Math.pow(10, 32)));
        Long winWeight = null;
        if (weightHeight == 0) {
            winWeight = (long) (new Random().nextInt(weightLow));
        } else if (weightHeight > 0) {
            winWeight = ((long) (new Random().nextInt(weightHeight)) << 32) + new Random().nextInt(weightLow);
        }
        if (winWeight == null) {
            return BaseResult.fail(i18nService.getMessage("20052"));
        }
        long currentWeight = 0L;
        LotteryDrawPrize winPrize = null;
        for (LotteryDrawPrize lotteryDrawPrize : lotteryDrawPrizeList) {
            currentWeight = currentWeight + lotteryDrawPrize.getWeight();
            if (winWeight < currentWeight) {
                winPrize = lotteryDrawPrize;
                break;
            }
        }
        LotteryDrawResp resp = new LotteryDrawResp();
        if (winPrize != null && winPrize.getPrizeUnit() != 0) {
            resp.setPrizeDepict(winPrize.getPrizeDepict());
            resp.setPrizeName(winPrize.getPrizeName());
            resp.setPrizeNum(winPrize.getPrizeNum());
            resp.setPrizeUnit(winPrize.getPrizeUnit());
            resp.setType(winPrize.getType());
            resp.setWinLottery(1);
        } else {
            resp.setType(lotteryDrawReq.getType());
            resp.setWinLottery(0);
        }
        if (lotteryDrawReq.getType() == 1) {
            lotteryCommonRecordService.saveLotteryCommon(tokenUser.getUserAddress(), lotteryDrawType, winPrize != null ? winPrize.getId() : null, resp.getWinLottery());
        } else if (lotteryDrawReq.getType() == 2) {
            lotteryRarityRecordService.saveLotteryRarity(tokenUser.getUserAddress(), lotteryDrawType, winPrize != null ? winPrize.getId() : null, resp.getWinLottery());
        } else if (lotteryDrawReq.getType() == 3) {
            lotteryEpicRecordService.saveLotteryEpic(tokenUser.getUserAddress(), lotteryDrawType, winPrize != null ? winPrize.getId() : null, resp.getWinLottery());
        }
        if (resp.getWinLottery() == 1) {
            if (resp.getPrizeUnit() == 0) {

            } else if (resp.getPrizeUnit() == 1) {
                uUserBaseInfoService.updateCredit(tokenUser.getUserAddress(), resp.getPrizeNum(), incomeType, null);
            } else if (resp.getPrizeUnit() == 2) {
                uUserBaseInfoService.updateMintTicket(tokenUser.getUserAddress(), resp.getPrizeNum(), incomeType, null);
            } else if (resp.getPrizeUnit() == 3) {
                uUserBaseInfoService.updateFreeTicket(tokenUser.getUserAddress(), resp.getPrizeNum(), incomeType, null);
            } else if (resp.getPrizeUnit() == 4) {
                uUserBaseInfoService.updateRarityTicket(tokenUser.getUserAddress(), resp.getPrizeNum().longValue(), incomeType, null);
            } else if (resp.getPrizeUnit() == 5) {
                uUserBaseInfoService.updateEpicTicket(tokenUser.getUserAddress(), resp.getPrizeNum().longValue(), incomeType, null);
            }
        }
        return BaseResult.success(resp);
    }


}
