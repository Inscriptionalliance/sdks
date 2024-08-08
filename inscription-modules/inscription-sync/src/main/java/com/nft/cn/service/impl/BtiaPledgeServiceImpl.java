package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.BtiaPledgeMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.Hex;
import com.nft.cn.vo.resp.BtiaPledgeResp;
import com.nft.cn.vo.resp.PledgeInfoResp;
import com.nft.cn.vo.resp.PledgeStatusResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BtiaPledgeServiceImpl extends ServiceImpl<BtiaPledgeMapper, BtiaPledge> implements BtiaPledgeService {

    @Autowired
    private BtiaIncomeService btiaIncomeService;
    @Autowired
    private SSystemConfigService sSystemConfigService;

    @Override
    public void btiaPledgeAward() {
        List<BtiaPledge> list = lambdaQuery().eq(BtiaPledge::getStatus, 1).list();
        String mintBtiaPledgeReleaseRatio = sSystemConfigService.getByKey(SystemConfigConstant.mint_btia_pledge_release_ratio).getConfigValue();
        for (BtiaPledge btiaPledge : list) {
            BigDecimal income = btiaPledge.getAmountAll().multiply(new BigDecimal(mintBtiaPledgeReleaseRatio)).setScale(0, RoundingMode.DOWN);
            if (income.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            if (income.compareTo(btiaPledge.getAmountResidue()) > 0) {
                income = btiaPledge.getAmountResidue();
            }
            BigDecimal amountResidueNew = btiaPledge.getAmountResidue().subtract(income);
            BigDecimal amountReleaseNew = btiaPledge.getAmountRelease().add(income);
            int status = 1;
            if (amountResidueNew.compareTo(BigDecimal.ZERO) <= 0) {
                status = 0;
            }
            boolean update = lambdaUpdate()
                    .set(BtiaPledge::getAmountResidue, amountResidueNew)
                    .set(BtiaPledge::getAmountRelease, amountReleaseNew)
                    .set(BtiaPledge::getStatus, status)
                    .set(BtiaPledge::getUpdateTime, LocalDateTime.now())

                    .eq(BtiaPledge::getId, btiaPledge.getId())
                    .update();
            if (update) {
                BtiaIncome btiaIncome = new BtiaIncome();
                btiaIncome.setUserId(btiaPledge.getUserId());
                btiaIncome.setUserAddress(btiaPledge.getUserAddress());
                btiaIncome.setDeployId(btiaPledge.getDeployId());
                btiaIncome.setAccord(btiaPledge.getAccord());
                btiaIncome.setTick(btiaPledge.getTick());
                btiaIncome.setAmountResidue(amountResidueNew);
                btiaIncome.setAmountRelease(income);
                btiaIncome.setStatus(0);
                btiaIncome.setUpdateTime(LocalDateTime.now());
                btiaIncome.setCreateTime(LocalDateTime.now());
                btiaIncomeService.save(btiaIncome);
            }

        }

    }
}
