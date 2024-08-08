package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.MintCommunityTeamAchieveMapper;
import com.nft.cn.dao.MintUserMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MintCommunityTeamAchieveServiceImpl extends ServiceImpl<MintCommunityTeamAchieveMapper, MintCommunityTeamAchieve> implements MintCommunityTeamAchieveService {

    @Autowired
    private UUserRefereeService uUserRefereeService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private SSystemStatisticsService sSystemStatisticsService;
    @Autowired
    private MintCommunityAddService mintCommunityAddService;
    @Autowired
    private MintUserMapper mintUserMapper;
    @Autowired
    private MintUserService mintUserService;

    @Override
    public void mintCommunityScheduled() {

        String startAdd = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_community_add_start).getStatisticsValue();
        if (startAdd.equalsIgnoreCase("0")) {
            return;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String statisticsValue = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_community_add_update_time).getStatisticsValue();
        if (statisticsValue.equalsIgnoreCase("0")) {
            statisticsValue = "2023-01-01 00:00:00";
        }
        List<MintUser> mintUserList = mintUserMapper.syncAchieve(statisticsValue, null);
        for (MintUser mintUser : mintUserList) {
            MintCommunityAdd mintCommunityAdd = new MintCommunityAdd();
            mintCommunityAdd.setUserId(mintUser.getUserId());
            mintCommunityAdd.setUserAddress(mintUser.getUserAddress());
            mintCommunityAdd.setMintNum(mintUser.getMintNum());
            mintCommunityAdd.setPaidMintNum(mintUser.getMintTicket().subtract(mintUser.getFreeTicket()).multiply(new BigDecimal("10000")));
            updateUserAchievement(mintCommunityAdd);
            mintUserService.lambdaUpdate()
                    .set(MintUser::getIsSync, 1)
                    .eq(MintUser::getId, mintUser.getId())
                    .update();
            sSystemStatisticsService.updateStatistics(SystemStatisticsConstant.mint_community_add_update_time, mintUser.getUpdateTime().format(dateTimeFormatter));
        }



    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserAchievement(MintCommunityAdd mintCommunityAdd) {
        MintCommunityTeamAchieve byId = getByUserId(mintCommunityAdd.getUserId());
        if (byId == null) {
            byId = new MintCommunityTeamAchieve();
            byId.setUserId(mintCommunityAdd.getUserId());
            byId.setUserAddress(mintCommunityAdd.getUserAddress());
            byId.setMintNum(mintCommunityAdd.getMintNum());
            byId.setPaidMintNum(mintCommunityAdd.getPaidMintNum());
            byId.setCommunityMintNumMax(BigDecimal.ZERO);
            byId.setCommunityPaidMintNumMax(BigDecimal.ZERO);
            byId.setCommunityMintNum(BigDecimal.ZERO);
            byId.setCommunityPaidMintNum(BigDecimal.ZERO);
            byId.setTeamMintNum(BigDecimal.ZERO);
            byId.setTeamPaidMintNum(BigDecimal.ZERO);
            byId.setRefereeMintNum(BigDecimal.ZERO);
            byId.setRefereePaidMintNum(BigDecimal.ZERO);
            byId.setIsInit(1);
            byId.setCreateTime(LocalDateTime.now());
            byId.setUpdateTime(LocalDateTime.now());
            save(byId);
        } else {
            byId.setMintNum(byId.getMintNum().add(mintCommunityAdd.getMintNum()));
            byId.setPaidMintNum(byId.getPaidMintNum().add(mintCommunityAdd.getPaidMintNum()));
            byId.setUpdateTime(LocalDateTime.now());
            updateByUserId(byId);
        }
        refreshAchieve(byId, mintCommunityAdd);
    }


    private void refreshAchieve(MintCommunityTeamAchieve mintCommunityTeamAchieve, MintCommunityAdd mintCommunityAdd) {
        List<MintCommunityTeamAchieve> list = baseMapper.selectUserIdList(mintCommunityTeamAchieve.getUserId());
        int era = 0;
        for (MintCommunityTeamAchieve byId : list) {
            era++;
            byId.setTeamMintNum(byId.getTeamMintNum().add(mintCommunityAdd.getMintNum()));
            byId.setTeamPaidMintNum(byId.getTeamPaidMintNum().add(mintCommunityAdd.getPaidMintNum()));
            if (era == 1) {
                byId.setRefereeMintNum(byId.getRefereeMintNum().add(mintCommunityAdd.getMintNum()));
                byId.setRefereePaidMintNum(byId.getRefereePaidMintNum().add(mintCommunityAdd.getPaidMintNum()));
            }
        }
        updateBatchById(list);
    }

    private MintCommunityTeamAchieve getByUserId(long userId) {
        return baseMapper.getByUserId(userId);
    }

    private boolean updateByUserId(MintCommunityTeamAchieve mintCommunityTeamAchieve) {
        return lambdaUpdate()
                .set(MintCommunityTeamAchieve::getUserAddress, mintCommunityTeamAchieve.getUserAddress())
                .set(MintCommunityTeamAchieve::getMintNum, mintCommunityTeamAchieve.getMintNum())
                .set(MintCommunityTeamAchieve::getPaidMintNum, mintCommunityTeamAchieve.getPaidMintNum())
                .set(MintCommunityTeamAchieve::getCommunityUserIdMax, mintCommunityTeamAchieve.getCommunityUserIdMax())
                .set(MintCommunityTeamAchieve::getCommunityUserAddressMax, mintCommunityTeamAchieve.getCommunityUserAddressMax())
                .set(MintCommunityTeamAchieve::getCommunityUserIdPaidMax, mintCommunityTeamAchieve.getCommunityUserIdPaidMax())
                .set(MintCommunityTeamAchieve::getCommunityUserAddressPaidMax, mintCommunityTeamAchieve.getCommunityUserAddressPaidMax())
                .set(MintCommunityTeamAchieve::getCommunityMintNumMax, mintCommunityTeamAchieve.getCommunityMintNumMax())
                .set(MintCommunityTeamAchieve::getCommunityPaidMintNumMax, mintCommunityTeamAchieve.getCommunityPaidMintNumMax())
                .set(MintCommunityTeamAchieve::getCommunityMintNum, mintCommunityTeamAchieve.getCommunityMintNum())
                .set(MintCommunityTeamAchieve::getCommunityPaidMintNum, mintCommunityTeamAchieve.getCommunityPaidMintNum())
                .set(MintCommunityTeamAchieve::getTeamMintNum, mintCommunityTeamAchieve.getTeamMintNum())
                .set(MintCommunityTeamAchieve::getTeamPaidMintNum, mintCommunityTeamAchieve.getTeamPaidMintNum())
                .set(MintCommunityTeamAchieve::getRefereeMintNum, mintCommunityTeamAchieve.getRefereeMintNum())
                .set(MintCommunityTeamAchieve::getRefereePaidMintNum, mintCommunityTeamAchieve.getRefereePaidMintNum())
                .set(MintCommunityTeamAchieve::getIsInit, mintCommunityTeamAchieve.getIsInit())
                .set(MintCommunityTeamAchieve::getCreateTime, mintCommunityTeamAchieve.getCreateTime())
                .set(MintCommunityTeamAchieve::getUpdateTime, LocalDateTime.now())
                .eq(MintCommunityTeamAchieve::getUserId, mintCommunityTeamAchieve.getUserId())
                .update();
    }



}
