package com.nft.cn.dao;

import com.nft.cn.entity.UUserAuthNum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UUserAuthNumMapper extends BaseMapper<UUserAuthNum> {

    void removeAuth();

}
