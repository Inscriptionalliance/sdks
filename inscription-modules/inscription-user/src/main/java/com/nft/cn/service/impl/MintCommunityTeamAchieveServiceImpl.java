package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintCommunityTeamAchieveMapper;
import com.nft.cn.entity.MintCommunityTeamAchieve;
import com.nft.cn.entity.UUserBaseInfo;
import com.nft.cn.service.MintCommunityTeamAchieveService;
import com.nft.cn.service.UUserBaseInfoService;
import com.nft.cn.vo.resp.VipUserInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MintCommunityTeamAchieveServiceImpl extends ServiceImpl<MintCommunityTeamAchieveMapper, MintCommunityTeamAchieve> implements MintCommunityTeamAchieveService {

    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;

    @Override
    public List<MintCommunityTeamAchieve> getAllSubordinate(Long userId) {
        return baseMapper.getAllSubordinate(userId);
    }

    @Override
    public List<MintCommunityTeamAchieve> getRefereeList(Long userId) {
        return baseMapper.getRefereeList(userId);
    }

    @Override
    public List<MintCommunityTeamAchieve> getRefereeListAll(Long userId) {
        return baseMapper.getRefereeListAll(userId);
    }

    @Override
    public Integer getAllSubordinateCount(Long userId) {
        return baseMapper.getAllSubordinateCount(userId);
    }

    @Override
    public BigDecimal selectAchieve(MintCommunityTeamAchieve mintCommunityTeamAchieve) {
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(mintCommunityTeamAchieve.getUserId());
        if (uUserBaseInfo.getIsVip() == 1 || uUserBaseInfo.getIsAdvancedVip() == 1) {
            return BigDecimal.ZERO;
        }
        BigDecimal vipAchieve = baseMapper.selectVipAchieve(mintCommunityTeamAchieve.getUserId());
        if (vipAchieve == null) {
            vipAchieve = BigDecimal.ZERO;
        }
        return mintCommunityTeamAchieve.getTeamPaidMintNum().subtract(vipAchieve).add(mintCommunityTeamAchieve.getPaidMintNum());
    }

    @Override
    public VipUserInfoResp selectAllAchieve(Long userId, MintCommunityTeamAchieve mintCommunityTeamAchieve, VipUserInfoResp vipUserInfoResp) {
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(userId);
        if (uUserBaseInfo.getIsVip() == 1 || uUserBaseInfo.getIsVip() == 3) {
            return vipUserInfoResp;
        }
        if (mintCommunityTeamAchieve.getPaidMintNum().compareTo(BigDecimal.ZERO) > 0) {
            vipUserInfoResp.setPaidMintNum(vipUserInfoResp.getPaidMintNum().add(mintCommunityTeamAchieve.getPaidMintNum()));
            vipUserInfoResp.setCommunityPartAddress(vipUserInfoResp.getCommunityPartAddress() + 1);
        }

        List<MintCommunityTeamAchieve> refereeList = getRefereeListAll(userId);
        if (!CollectionUtils.isEmpty(refereeList)) {
            for (MintCommunityTeamAchieve achieve : refereeList) {
                selectAllAchieve(achieve.getUserId(), achieve,vipUserInfoResp);
            }
        }
        return vipUserInfoResp;
    }

    @Override
    public List<MintCommunityTeamAchieve> selectChild(Long userId) {
        return baseMapper.selectChild(userId);
    }

    @Override
    public BigDecimal sumByUserIdList(List<Long> userIdList) {
        return baseMapper.sumByUserIdList(userIdList);
    }

    @Override
    public List<MintCommunityTeamAchieve> selectByUserIdList(List<Long> userIdList) {
        return baseMapper.selectByUserIdList(userIdList);
    }
}
