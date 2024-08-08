package com.nft.cn.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.cn.entity.MintRankUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface MintRankUserMapper extends BaseMapper<MintRankUser> {

    Page<MintRankUser> selectRefereeAchieve(Page<MintRankUser> page, @Param("userAddress") String userAddress, @Param("achieve") BigDecimal achieve, @Param("phase") Integer phase);

}
