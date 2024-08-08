package com.nft.cn.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.cn.entity.MintRank;
import com.nft.cn.entity.MintRankUser;
import com.nft.cn.entity.MintUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface MintUserMapper extends BaseMapper<MintUser> {

    Page<MintUser> getMintRankPage(Page<MintUser> page);

    List<MintUser> getMintRank();

    Page<MintRankUser> selectMintRankPage(Page<MintRankUser> page, @Param("userAddress") String userAddress);

    BigDecimal sumMintNum(@Param("userId") Long userId, @Param("status") Integer status);

    BigDecimal sumTeamMintNum(@Param("userId") Long userId, @Param("status") Integer status);

    List<MintUser> syncAchieve(@Param("updateTime") String updateTime, @Param("limit") Integer limit);

    BigDecimal selectAchieveMint(@Param("userId") Long userId);

    MintUser sumTeamMintUser(@Param("userId") Long userId, @Param("status") Integer status);

    MintUser selectRefereeMint(@Param("userId") Long id);

    MintUser sumMint(@Param("userId") Long userId);

    List<MintUser> selectAchieveChildMint(@Param("list") List<Long> list);

    BigDecimal selectYesterdayCommunity(@Param("userId") Long userId, @Param("bigUserId") Long bigUserId, @Param("now") String now);

    BigDecimal selectYesterdayReferee(@Param("userId") Long userId, @Param("now") String now);

}
