package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UserLoginDayMapper;
import com.nft.cn.entity.UserLoginDay;
import com.nft.cn.service.UserLoginDayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserLoginDayServiceImpl extends ServiceImpl<UserLoginDayMapper, UserLoginDay> implements UserLoginDayService {

    @Override
    public void saveDay(Integer activeNumDay, Integer addNumDay, LocalDate yesterday) {
        UserLoginDay userLoginDay = new UserLoginDay();
        userLoginDay.setActiveNum(activeNumDay.longValue());
        userLoginDay.setAddNum(addNumDay.longValue());
        userLoginDay.setCreateTime(LocalDateTime.now());
        long year = yesterday.getYear() * 10000;
        long monthValue = yesterday.getMonthValue() * 100;
        long dayOfMonth = yesterday.getDayOfMonth();
        userLoginDay.setDateNum(year + monthValue + dayOfMonth);
        save(userLoginDay);
    }
}
