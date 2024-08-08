package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UserLoginWeekMapper;
import com.nft.cn.entity.UserLoginWeek;
import com.nft.cn.service.UserLoginWeekService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserLoginWeekServiceImpl extends ServiceImpl<UserLoginWeekMapper, UserLoginWeek> implements UserLoginWeekService {

    @Override
    public void saveWeek(Integer activeNumWeek, Integer addNumWeek, LocalDate beforeWeekMonday, LocalDate nowDate) {
        UserLoginWeek userLoginWeek = new UserLoginWeek();
        userLoginWeek.setActiveNum(activeNumWeek.longValue());
        userLoginWeek.setAddNum(addNumWeek.longValue());
        userLoginWeek.setCreateTime(LocalDateTime.now());
        long year = beforeWeekMonday.getYear() * 1000000000000L;
        long monthValue = beforeWeekMonday.getMonthValue() * 10000000000L;
        long dayOfMonth = beforeWeekMonday.getDayOfMonth() * 100000000L;
        LocalDate localDate7 = nowDate.minusDays(1);
        long year7 = localDate7.getYear() * 10000;
        long monthValue7 = localDate7.getMonthValue() * 100;
        long dayOfMonth7 = localDate7.getDayOfMonth();
        userLoginWeek.setDateNum((year + monthValue + dayOfMonth + year7 + monthValue7 + dayOfMonth7));
        save(userLoginWeek);
    }
}
