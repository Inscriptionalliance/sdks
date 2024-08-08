package com.nft.cn.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.cn.entity.MintCommunityTeamAchieve;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nft.cn.entity.MintRankCommunity;
import com.nft.cn.entity.MintRankUser;
import com.nft.cn.vo.resp.MintRankUserResp;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MintCommunityTeamAchieveMapper extends BaseMapper<MintCommunityTeamAchieve> {


    MintCommunityTeamAchieve getByUserId(@Param("userId") long userId);


    List<MintCommunityTeamAchieve> getAllSubordinate(@Param("userId") Long userId);

    List<MintCommunityTeamAchieve> selectUserIdList(@Param("userId") Long userId);

    List<MintCommunityTeamAchieve> getRefereeList(@Param("userId") Long userId);

    Integer getAllSubordinateCount(@Param("userId") Long userId);

    List<MintCommunityTeamAchieve> getRefereeListAll(@Param("userId") Long userId);

    List<MintCommunityTeamAchieve> selectVipList(@Param("userId") Long userId);

    BigDecimal selectVipAchieve(@Param("userId") Long userId);

    List<MintCommunityTeamAchieve> selectChild(@Param("userId") Long userId);

    BigDecimal sumByUserIdList(@Param("list") List<Long> list);

    List<MintCommunityTeamAchieve> selectByUserIdList(@Param("list") List<Long> list);


}
