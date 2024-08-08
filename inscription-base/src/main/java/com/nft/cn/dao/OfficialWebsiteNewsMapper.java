package com.nft.cn.dao;

import com.nft.cn.entity.OfficialWebsiteNews;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface OfficialWebsiteNewsMapper extends BaseMapper<OfficialWebsiteNews> {

    OfficialWebsiteNews selectFirstNews();

    OfficialWebsiteNews selectFirstNewsAndImg();
}
