package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.SSystemStatisticsMapper;
import com.nft.cn.entity.SSystemStatistics;
import com.nft.cn.service.SSystemStatisticsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class SSystemStatisticsServiceImpl extends ServiceImpl<SSystemStatisticsMapper, SSystemStatistics> implements SSystemStatisticsService {


    @Override
    public SSystemStatistics getByKey(String key) {
        SSystemStatistics one = lambdaQuery().eq(SSystemStatistics::getStatisticsKey, key).one();
        if (one == null) {
            one = new SSystemStatistics();
            one.setStatisticsKey(key);
            one.setStatisticsValue("0");
            one.setRemark(key);
            one.setUpdateTime(LocalDateTime.now());
            one.setCreateTime(LocalDateTime.now());
            save(one);
        }
        return one;
    }

    @Override
    public void updateStatistics(String key, BigDecimal value, int operate) {
        while (true) {
            SSystemStatistics byKey = getByKey(key);
            String statisticsValue = byKey.getStatisticsValue();
            BigDecimal newValue = BigDecimal.ZERO;
            if (operate == 1) {
                newValue = new BigDecimal(statisticsValue).add(value);
            } else {
                newValue = new BigDecimal(statisticsValue).subtract(value);
            }
            boolean update = lambdaUpdate()
                    .set(SSystemStatistics::getStatisticsValue, newValue)
                    .set(SSystemStatistics::getUpdateTime, LocalDateTime.now())
                    .eq(SSystemStatistics::getStatisticsValue, byKey.getStatisticsValue())
                    .eq(SSystemStatistics::getStatisticsKey, key)
                    .update();
            if (update) {
                break;
            }

        }


    }

    @Override
    public void updateStatistics(String key, BigDecimal value) {
        SSystemStatistics byKey = getByKey(key);
        boolean update = lambdaUpdate()
                .set(SSystemStatistics::getStatisticsValue, value)
                .set(SSystemStatistics::getUpdateTime, LocalDateTime.now())
                .eq(SSystemStatistics::getStatisticsKey, key)
                .update();
    }

    @Override
    public void updateNonce(String key) {
        while (true) {
            SSystemStatistics byKey = getByKey(key);
            String statisticsValue = byKey.getStatisticsValue();
            BigDecimal newValue = new BigDecimal(statisticsValue).add(new BigDecimal("1"));
            boolean update = lambdaUpdate()
                    .set(SSystemStatistics::getStatisticsValue, newValue)
                    .set(SSystemStatistics::getUpdateTime, LocalDateTime.now())
                    .eq(SSystemStatistics::getStatisticsValue, byKey.getStatisticsValue())
                    .eq(SSystemStatistics::getStatisticsKey, key)
                    .update();
            if (update) {
                break;
            }
        }
    }


}
