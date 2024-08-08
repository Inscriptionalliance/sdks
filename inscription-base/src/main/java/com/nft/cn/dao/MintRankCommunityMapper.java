package com.nft.cn.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.cn.entity.MintRankCommunity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nft.cn.entity.MintUser;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MintRankCommunityMapper extends BaseMapper<MintRankCommunity> {

    Page<MintRankCommunity> achieveAllListGtNumPage(Page<MintRankCommunity> page, @Param("achieve") BigDecimal achieve, @Param("phase") Integer phase, @Param("list") List<String> list);

}
