package com.nft.cn.dao;

import com.nft.cn.entity.MintRank;
import com.nft.cn.entity.MintUserHold;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface MintUserHoldMapper extends BaseMapper<MintUserHold> {

    List<MintRank> getMintRank();

    void removeHold();

}
