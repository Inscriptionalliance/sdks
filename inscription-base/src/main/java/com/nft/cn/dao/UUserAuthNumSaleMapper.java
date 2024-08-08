package com.nft.cn.dao;

import com.nft.cn.entity.UUserAuthNumSale;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UUserAuthNumSaleMapper extends BaseMapper<UUserAuthNumSale> {

    void removeAuth();

}
