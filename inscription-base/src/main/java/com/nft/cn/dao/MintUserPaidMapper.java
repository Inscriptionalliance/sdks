package com.nft.cn.dao;

import com.nft.cn.entity.MintUserPaid;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MintUserPaidMapper extends BaseMapper<MintUserPaid> {

    Integer countPartNum(@Param("phase") int phase, @Param("list") List<String> list);

    BigDecimal sumMintNum(@Param("userId") Long userId,@Param("phase") int phase);

    BigDecimal sumTeamMintNum(@Param("userId") Long userId,@Param("phase") int phase);

    BigDecimal sumRefereeMintNum(@Param("userId") Long userId,@Param("phase") int phase);

    List<MintUserPaid> syncAchieve(@Param("updateTime") String updateTime);

    BigDecimal selectYesterdayCommunity(@Param("userId") Long userId, @Param("bigUserId") Long bigUserId, @Param("phase") Integer phase, @Param("now") String now);

    BigDecimal selectYesterdayReferee(@Param("userId") Long userId, @Param("phase") Integer phase, @Param("now") String now);

    List<MintUserPaid> selectYesterdayRefereeList(@Param("list") List<Long> list, @Param("phase") Integer phase, @Param("now") String now);

    List<MintUserPaid> selectYesterdayCommunityList(@Param("list") List<Long> list, @Param("phase") Integer phase, @Param("now") String now);

}
