package com.nft.cn.dao;

import com.nft.cn.entity.UUserRefereeNum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface UUserRefereeNumMapper extends BaseMapper<UUserRefereeNum> {

    List<UUserRefereeNum> selectUser50();

}
