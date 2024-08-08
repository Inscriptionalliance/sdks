package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintCommunityTeamAchieve;
import com.nft.cn.vo.resp.VipUserInfoResp;

import java.math.BigDecimal;
import java.util.List;

public interface MintCommunityTeamAchieveService extends IService<MintCommunityTeamAchieve> {

    List<MintCommunityTeamAchieve> getAllSubordinate(Long id);

    List<MintCommunityTeamAchieve>  getRefereeList(Long id);

    List<MintCommunityTeamAchieve>  getRefereeListAll(Long id);

    Integer getAllSubordinateCount(Long id);

    BigDecimal selectAchieve(MintCommunityTeamAchieve mintCommunityTeamAchieve);


    VipUserInfoResp selectAllAchieve(Long userId, MintCommunityTeamAchieve one, VipUserInfoResp vipUserInfoResp);


    List<MintCommunityTeamAchieve> selectChild(Long id);

    BigDecimal sumByUserIdList(List<Long> userIdList);

    List<MintCommunityTeamAchieve> selectByUserIdList(List<Long> userIdList);

}
