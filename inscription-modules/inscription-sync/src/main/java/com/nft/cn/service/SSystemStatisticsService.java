package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.SSystemStatistics;

import java.math.BigDecimal;

public interface SSystemStatisticsService extends IService<SSystemStatistics> {

    SSystemStatistics getByKey(String key);

    void updateStatistics(String key, BigDecimal value, int operate);

    void updateStatistics(String key, BigDecimal value);

    void updateStatistics(String key, String value);

    void updateNonce(String key);

}
