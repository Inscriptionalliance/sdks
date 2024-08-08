package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.MintUserPaidMapper;
import com.nft.cn.entity.MintDeploy;
import com.nft.cn.entity.MintUser;
import com.nft.cn.entity.MintUserPaid;
import com.nft.cn.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MintUserPaidServiceImpl extends ServiceImpl<MintUserPaidMapper, MintUserPaid> implements MintUserPaidService {

    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private MintDeployService mintDeployService;
    @Autowired
    private SSystemStatisticsService sSystemStatisticsService;

    @Override
    public void savePaidMint(Long mintUserId) {
        MintUser mintUser = mintUserService.getById(mintUserId);
        if (mintUser.getMintNum().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        MintUserPaid mintUserPaid = new MintUserPaid();
        BeanUtils.copyProperties(mintUser, mintUserPaid);
        mintUserPaid.setId(null);
        mintUserPaid.setCreateTime(LocalDateTime.now());
        mintUserPaid.setUpdateTime(LocalDateTime.now());
        BigDecimal mintTicketPaid = mintUser.getMintTicket().subtract(mintUser.getFreeTicket());
        if (mintTicketPaid.compareTo(BigDecimal.ZERO) < 0) {
            mintTicketPaid = BigDecimal.ZERO;
        }
        if (mintTicketPaid.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        mintUserPaid.setMintTicketPaid(mintTicketPaid);
        MintDeploy mintDeploy = mintDeployService.lambdaQuery().eq(MintDeploy::getAccord, mintUser.getAccord()).eq(MintDeploy::getTick, mintUser.getTick()).one();
        mintUserPaid.setMintNum(mintTicketPaid.multiply(mintDeploy.getMintOne()));
        mintUserPaid.setPhase(0);
        save(mintUserPaid);
        String mintMintIncomePhase = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue();
        String mintMintIncomePhaseNum = sSystemConfigService.getByKey(SystemConfigConstant.mint_mint_income_phase_num).getConfigValue();
        for (int i = 1; i < (Integer.parseInt(mintMintIncomePhaseNum) + 1); i++) {
            MintUserPaid mintUserPaidSum = getOne(new QueryWrapper<MintUserPaid>().select("sum(mint_num) as mintNum").eq("phase", i));
            if (mintUserPaidSum == null) {
                mintUserPaidSum = new MintUserPaid();
                mintUserPaidSum.setMintNum(BigDecimal.ZERO);
            }
            BigDecimal mintTicketSum = mintUserPaidSum.getMintNum().divide(mintDeploy.getMintOne(), 0);
            BigDecimal mintPhaseMintNum = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.mint_phase_mint_num_ + i).getConfigValue());
            if (mintTicketSum.compareTo(mintPhaseMintNum) < 0) {
                if (Integer.parseInt(mintMintIncomePhase) < i) {
                    sSystemStatisticsService.updateStatistics(SystemStatisticsConstant.mint_mint_income_phase, new BigDecimal(i));
                }
                BigDecimal subtract = mintPhaseMintNum.subtract(mintTicketSum);
                if (mintTicketPaid.compareTo(subtract) <= 0) {
                    mintUserPaid.setId(null);
                    mintUserPaid.setMintTicketPaid(mintTicketPaid);
                    mintUserPaid.setMintNum(mintTicketPaid.multiply(mintDeploy.getMintOne()));
                    mintUserPaid.setPhase(i);
                    save(mintUserPaid);
                    break;
                } else {
                    mintTicketPaid = mintTicketPaid.subtract(subtract);
                    mintUserPaid.setId(null);
                    mintUserPaid.setMintTicketPaid(subtract);
                    mintUserPaid.setMintNum(subtract.multiply(mintDeploy.getMintOne()));
                    mintUserPaid.setPhase(i);
                    save(mintUserPaid);
                    continue;
                }
            } else {
                continue;
            }
        }



    }

    @Override
    public BigDecimal sumMintNum(Long userId, int phase) {
        return baseMapper.sumMintNum(userId, phase);
    }

    @Override
    public BigDecimal sumTeamMintNum(Long userId, int phase) {
        return baseMapper.sumTeamMintNum(userId, phase);
    }

    @Override
    public BigDecimal sumRefereeMintNum(Long userId, int phase) {
        return baseMapper.sumRefereeMintNum(userId, phase);
    }

    @Override
    public List<MintUserPaid> syncAchieve(String updateTime) {
        return baseMapper.syncAchieve(updateTime);
    }
}
