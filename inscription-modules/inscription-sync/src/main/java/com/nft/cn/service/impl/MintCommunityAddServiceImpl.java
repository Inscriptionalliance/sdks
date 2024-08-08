package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintCommunityAddMapper;
import com.nft.cn.entity.MintCommunityAdd;
import com.nft.cn.entity.MintCommunityTeamAchieve;
import com.nft.cn.service.MintCommunityAddService;
import com.nft.cn.service.MintCommunityTeamAchieveService;
import com.nft.cn.service.MintUserPaidService;
import com.nft.cn.service.MintUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;

@Service
public class MintCommunityAddServiceImpl extends ServiceImpl<MintCommunityAddMapper, MintCommunityAdd> implements MintCommunityAddService {

    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private MintUserPaidService mintUserPaidService;
    @Autowired
    private MintCommunityTeamAchieveService mintCommunityTeamAchieveService;

    @Override
    public void saveCommunityAddRunnable(Long userId, String userAddress, BigDecimal mintNum, BigDecimal paidMintNum) {
        MintCommunityTeamAchieve byId = mintCommunityTeamAchieveService.getById(userId);
        MintCommunityAdd mintCommunityAdd = new MintCommunityAdd();
        mintCommunityAdd.setUserId(userId);
        mintCommunityAdd.setUserAddress(userAddress);
        mintCommunityAdd.setCreateTime(LocalDateTime.now());
        mintCommunityAdd.setUpdateTime(LocalDateTime.now());
        if (byId != null && byId.getIsInit() == 1) {
            mintCommunityAdd.setMintNum(mintNum);
            mintCommunityAdd.setPaidMintNum(paidMintNum);
        } else {
            BigDecimal sumMintNum = mintUserService.sumMintNum(userId, 4);
            if (sumMintNum == null) {
                sumMintNum = BigDecimal.ZERO;
            }
            BigDecimal sumMintNumPaid = mintUserPaidService.sumMintNum(userId, 0);
            if (sumMintNumPaid == null) {
                sumMintNumPaid = BigDecimal.ZERO;
            }
            mintCommunityAdd.setMintNum(sumMintNum);
            mintCommunityAdd.setPaidMintNum(sumMintNumPaid);
        }
        save(mintCommunityAdd);
    }
}
