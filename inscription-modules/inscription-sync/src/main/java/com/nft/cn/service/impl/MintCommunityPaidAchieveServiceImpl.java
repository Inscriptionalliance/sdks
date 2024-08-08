package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.MintCommunityPaidAchieveMapper;
import com.nft.cn.dao.MintUserMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class MintCommunityPaidAchieveServiceImpl extends ServiceImpl<MintCommunityPaidAchieveMapper, MintCommunityPaidAchieve> implements MintCommunityPaidAchieveService {

    @Autowired
    private SSystemStatisticsService sSystemStatisticsService;
    @Autowired
    private MintUserPaidService mintUserPaidService;
    @Autowired
    private UUserRefereeService uUserRefereeService;
    @Autowired
    private UUserService uUserService;

    @Override
    public void mintCommunityPaidScheduled() {
        String startAdd = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_community_paid_add_start).getStatisticsValue();
        if (startAdd.equalsIgnoreCase("0")) {
            return;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String statisticsValue = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_community_paid_add_update_time).getStatisticsValue();
        if (statisticsValue.equalsIgnoreCase("0")) {
            statisticsValue = "2023-01-01 00:00:00";
        }
        List<MintUserPaid> mintUserPaidList = mintUserPaidService.syncAchieve(statisticsValue);
        for (MintUserPaid mintUserPaid : mintUserPaidList) {
            updateUserAchievement(mintUserPaid);
            mintUserPaidService.lambdaUpdate()
                    .set(MintUserPaid::getIsSync, 1)
                    .eq(MintUserPaid::getId, mintUserPaid.getId())
                    .update();
            sSystemStatisticsService.updateStatistics(SystemStatisticsConstant.mint_community_paid_add_update_time, mintUserPaid.getUpdateTime().format(dateTimeFormatter));
        }

    }


    @Transactional(rollbackFor = Exception.class)
    public void updateUserAchievement(MintUserPaid mintUserPaid) {
        MintCommunityPaidAchieve byId = getByUserId(mintUserPaid.getUserId());
        if (byId == null) {
            byId = new MintCommunityPaidAchieve();
            byId.setUserId(mintUserPaid.getUserId());
            byId.setUserAddress(mintUserPaid.getUserAddress());
            byId.setPaidMintNum(BigDecimal.ZERO);
            byId.setPaidMintNum1(BigDecimal.ZERO);
            byId.setPaidMintNum2(BigDecimal.ZERO);
            byId.setPaidMintNum3(BigDecimal.ZERO);
            byId.setPaidMintNum4(BigDecimal.ZERO);
            if (mintUserPaid.getPhase() == 0) {
                byId.setPaidMintNum(mintUserPaid.getMintNum());
            } else if (mintUserPaid.getPhase() == 1) {
                byId.setPaidMintNum1(mintUserPaid.getMintNum());
            } else if (mintUserPaid.getPhase() == 2) {
                byId.setPaidMintNum2(mintUserPaid.getMintNum());
            } else if (mintUserPaid.getPhase() == 3) {
                byId.setPaidMintNum3(mintUserPaid.getMintNum());
            } else if (mintUserPaid.getPhase() == 4) {
                byId.setPaidMintNum4(mintUserPaid.getMintNum());
            }
            byId.setTeamPaidMintNum(BigDecimal.ZERO);
            byId.setTeamPaidMintNum1(BigDecimal.ZERO);
            byId.setTeamPaidMintNum2(BigDecimal.ZERO);
            byId.setTeamPaidMintNum3(BigDecimal.ZERO);
            byId.setTeamPaidMintNum4(BigDecimal.ZERO);
            byId.setRefereePaidMintNum(BigDecimal.ZERO);
            byId.setRefereePaidMintNum1(BigDecimal.ZERO);
            byId.setRefereePaidMintNum2(BigDecimal.ZERO);
            byId.setRefereePaidMintNum3(BigDecimal.ZERO);
            byId.setRefereePaidMintNum4(BigDecimal.ZERO);
            byId.setIsInit(1);
            byId.setCreateTime(LocalDateTime.now());
            byId.setUpdateTime(LocalDateTime.now());
            save(byId);
        } else {
            if (mintUserPaid.getPhase() == 0) {
                byId.setPaidMintNum(byId.getPaidMintNum().add(mintUserPaid.getMintNum()));
            } else if (mintUserPaid.getPhase() == 1) {
                byId.setPaidMintNum1(byId.getPaidMintNum1().add(mintUserPaid.getMintNum()));
            } else if (mintUserPaid.getPhase() == 2) {
                byId.setPaidMintNum2(byId.getPaidMintNum2().add(mintUserPaid.getMintNum()));
            } else if (mintUserPaid.getPhase() == 3) {
                byId.setPaidMintNum3(byId.getPaidMintNum3().add(mintUserPaid.getMintNum()));
            } else if (mintUserPaid.getPhase() == 4) {
                byId.setPaidMintNum4(byId.getPaidMintNum4().add(mintUserPaid.getMintNum()));
            }
            byId.setUpdateTime(LocalDateTime.now());
            updateByUserId(byId);
        }
        refreshAchieve(byId, mintUserPaid);
    }



    private void refreshAchieve(MintCommunityPaidAchieve mintCommunityPaidAchieve, MintUserPaid mintUserPaid) {
        UUserReferee uUserReferee = uUserRefereeService.lambdaQuery().eq(UUserReferee::getUserId, mintCommunityPaidAchieve.getUserId()).one();
        String refereeRelation = uUserReferee.getRefereeRelation();
        if (StringUtils.isEmpty(refereeRelation)) {
            return;
        }
        String[] split = refereeRelation.split(",");
        int area = 0;
        for (int i = split.length - 1; i >= 0; i--) {
            area++;
            long userId = Long.parseLong(split[i]);
            MintCommunityPaidAchieve byId = lambdaQuery().eq(MintCommunityPaidAchieve::getUserId, userId).one();
            if (byId == null) {
                UUser uUser = uUserService.getById(userId);
                byId = new MintCommunityPaidAchieve();
                byId.setUserId(uUser.getId());
                byId.setUserAddress(uUser.getUserAddress());
                byId.setPaidMintNum(BigDecimal.ZERO);
                byId.setPaidMintNum1(BigDecimal.ZERO);
                byId.setPaidMintNum2(BigDecimal.ZERO);
                byId.setPaidMintNum3(BigDecimal.ZERO);
                byId.setPaidMintNum4(BigDecimal.ZERO);
                byId.setTeamPaidMintNum(BigDecimal.ZERO);
                byId.setTeamPaidMintNum1(BigDecimal.ZERO);
                byId.setTeamPaidMintNum2(BigDecimal.ZERO);
                byId.setTeamPaidMintNum3(BigDecimal.ZERO);
                byId.setTeamPaidMintNum4(BigDecimal.ZERO);
                byId.setRefereePaidMintNum(BigDecimal.ZERO);
                byId.setRefereePaidMintNum1(BigDecimal.ZERO);
                byId.setRefereePaidMintNum2(BigDecimal.ZERO);
                byId.setRefereePaidMintNum3(BigDecimal.ZERO);
                byId.setRefereePaidMintNum4(BigDecimal.ZERO);
                byId.setIsInit(1);
                byId.setCreateTime(LocalDateTime.now());
                byId.setUpdateTime(LocalDateTime.now());
                save(byId);
            }

            if (mintUserPaid.getPhase() == 0) {
                byId.setTeamPaidMintNum(byId.getTeamPaidMintNum().add(mintUserPaid.getMintNum()));
            } else if (mintUserPaid.getPhase() == 1) {
                byId.setTeamPaidMintNum1(byId.getTeamPaidMintNum1().add(mintUserPaid.getMintNum()));
            } else if (mintUserPaid.getPhase() == 2) {
                byId.setTeamPaidMintNum2(byId.getTeamPaidMintNum2().add(mintUserPaid.getMintNum()));
            } else if (mintUserPaid.getPhase() == 3) {
                byId.setTeamPaidMintNum3(byId.getTeamPaidMintNum3().add(mintUserPaid.getMintNum()));
            } else if (mintUserPaid.getPhase() == 4) {
                byId.setTeamPaidMintNum4(byId.getTeamPaidMintNum4().add(mintUserPaid.getMintNum()));
            }
            if (area == 1) {
                if (mintUserPaid.getPhase() == 0) {
                    byId.setRefereePaidMintNum(byId.getRefereePaidMintNum().add(mintUserPaid.getMintNum()));
                } else if (mintUserPaid.getPhase() == 1) {
                    byId.setRefereePaidMintNum1(byId.getRefereePaidMintNum1().add(mintUserPaid.getMintNum()));
                } else if (mintUserPaid.getPhase() == 2) {
                    byId.setRefereePaidMintNum2(byId.getRefereePaidMintNum2().add(mintUserPaid.getMintNum()));
                } else if (mintUserPaid.getPhase() == 3) {
                    byId.setRefereePaidMintNum3(byId.getRefereePaidMintNum3().add(mintUserPaid.getMintNum()));
                } else if (mintUserPaid.getPhase() == 4) {
                    byId.setRefereePaidMintNum4(byId.getRefereePaidMintNum4().add(mintUserPaid.getMintNum()));
                }
            }
            updateByUserId(byId);
        }
    }


    private MintCommunityPaidAchieve getByUserId(long userId) {
        return baseMapper.getByUserId(userId);
    }

    private boolean updateByUserId(MintCommunityPaidAchieve mintCommunityPaidAchieve) {
        return lambdaUpdate()
                .set(MintCommunityPaidAchieve::getUserAddress, mintCommunityPaidAchieve.getUserAddress())
                .set(MintCommunityPaidAchieve::getPaidMintNum, mintCommunityPaidAchieve.getPaidMintNum())
                .set(MintCommunityPaidAchieve::getPaidMintNum1, mintCommunityPaidAchieve.getPaidMintNum1())
                .set(MintCommunityPaidAchieve::getPaidMintNum2, mintCommunityPaidAchieve.getPaidMintNum2())
                .set(MintCommunityPaidAchieve::getPaidMintNum3, mintCommunityPaidAchieve.getPaidMintNum3())
                .set(MintCommunityPaidAchieve::getPaidMintNum4, mintCommunityPaidAchieve.getPaidMintNum4())
                .set(MintCommunityPaidAchieve::getTeamPaidMintNum, mintCommunityPaidAchieve.getTeamPaidMintNum())
                .set(MintCommunityPaidAchieve::getTeamPaidMintNum1, mintCommunityPaidAchieve.getTeamPaidMintNum1())
                .set(MintCommunityPaidAchieve::getTeamPaidMintNum2, mintCommunityPaidAchieve.getTeamPaidMintNum2())
                .set(MintCommunityPaidAchieve::getTeamPaidMintNum3, mintCommunityPaidAchieve.getTeamPaidMintNum3())
                .set(MintCommunityPaidAchieve::getTeamPaidMintNum4, mintCommunityPaidAchieve.getTeamPaidMintNum4())
                .set(MintCommunityPaidAchieve::getTeamPaidMintNum, mintCommunityPaidAchieve.getTeamPaidMintNum())
                .set(MintCommunityPaidAchieve::getRefereePaidMintNum, mintCommunityPaidAchieve.getRefereePaidMintNum())
                .set(MintCommunityPaidAchieve::getRefereePaidMintNum1, mintCommunityPaidAchieve.getRefereePaidMintNum1())
                .set(MintCommunityPaidAchieve::getRefereePaidMintNum2, mintCommunityPaidAchieve.getRefereePaidMintNum2())
                .set(MintCommunityPaidAchieve::getRefereePaidMintNum3, mintCommunityPaidAchieve.getRefereePaidMintNum3())
                .set(MintCommunityPaidAchieve::getRefereePaidMintNum4, mintCommunityPaidAchieve.getRefereePaidMintNum4())
                .set(MintCommunityPaidAchieve::getIsInit, mintCommunityPaidAchieve.getIsInit())
                .set(MintCommunityPaidAchieve::getCreateTime, mintCommunityPaidAchieve.getCreateTime())
                .set(MintCommunityPaidAchieve::getUpdateTime, LocalDateTime.now())
                .eq(MintCommunityPaidAchieve::getUserId, mintCommunityPaidAchieve.getUserId())
                .update();
    }


}
