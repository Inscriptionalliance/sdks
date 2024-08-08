package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UserLoginMonthMapper;
import com.nft.cn.entity.UserLoginMonth;
import com.nft.cn.service.UserLoginMonthService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserLoginMonthServiceImpl extends ServiceImpl<UserLoginMonthMapper, UserLoginMonth> implements UserLoginMonthService {

    @Override
    public void saveMonth(Integer activeNumMonth, Integer addNumMonth, LocalDate beforeMonthStart, LocalDate nowDate) {
        UserLoginMonth userLoginMonth = new UserLoginMonth();
        userLoginMonth.setActiveNum(activeNumMonth.longValue());
        userLoginMonth.setAddNum(addNumMonth.longValue());
        userLoginMonth.setCreateTime(LocalDateTime.now());
        long year = beforeMonthStart.getYear() * 1000000000000L;
        long monthValue = beforeMonthStart.getMonthValue() * 10000000000L;
        long dayOfMonth = beforeMonthStart.getDayOfMonth() * 100000000L;
        LocalDate localDate7 = nowDate.minusDays(1);
        long year7 = localDate7.getYear() * 10000;
        long monthValue7 = localDate7.getMonthValue() * 100;
        long dayOfMonth7 = localDate7.getDayOfMonth();
        userLoginMonth.setDateNum((year + monthValue + dayOfMonth + year7 + monthValue7 + dayOfMonth7));
        save(userLoginMonth);
    }
}
