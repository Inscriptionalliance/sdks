package com.nft.cn.dao;

import com.nft.cn.entity.UUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface UUserMapper extends BaseMapper<UUser> {

    List<UUser> selectChildUser(Long id);

}
