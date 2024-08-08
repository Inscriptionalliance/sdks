package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.SSystemConfigMapper;
import com.nft.cn.entity.SSystemConfig;
import com.nft.cn.service.SSystemConfigService;
import org.springframework.stereotype.Service;

@Service
public class SSystemConfigServiceImpl extends ServiceImpl<SSystemConfigMapper, SSystemConfig> implements SSystemConfigService {

    @Override
    public SSystemConfig getByKey(String key) {
        return lambdaQuery().eq(SSystemConfig::getConfigKey, key).one();
    }


}
