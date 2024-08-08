package com.nft.cn.dao;

import com.nft.cn.entity.MintCommunityPaidAchieve;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MintCommunityPaidAchieveMapper extends BaseMapper<MintCommunityPaidAchieve> {

    MintCommunityPaidAchieve getByUserId(@Param("userId") long userId);

    List<MintCommunityPaidAchieve> selectChild(@Param("userId") Long userId);

    Integer getAllSubordinateCount(@Param("userId") Long userId, @Param("phase") Integer phase);
}
