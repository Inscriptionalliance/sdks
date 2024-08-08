package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.SSystemConfig;

public interface SSystemConfigService extends IService<SSystemConfig> {


    SSystemConfig getByKey(String key);

}
