package com.nft.cn.scheduled;

import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UserLogin;
import com.nft.cn.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class UserLoginScheduled {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UserLoginDayService userLoginDayService;
    @Autowired
    private UserLoginWeekService userLoginWeekService;
    @Autowired
    private UserLoginMonthService userLoginMonthService;


//    @Scheduled(cron = "0 0 0 * * ?")
    public void replaceTweetsAward() throws Throwable {
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDate nowDate = nowDateTime.toLocalDate();
        LocalDate yesterday = nowDate.minusDays(1);
        Integer activeNumDay = userLoginService.lambdaQuery().ge(UserLogin::getCreateTime, yesterday).lt(UserLogin::getCreateTime, nowDate).count();
        if (activeNumDay == null) {
            activeNumDay = 0;
        }
        Integer addNumDay = uUserService.lambdaQuery().ge(UUser::getCreateTime, yesterday).lt(UUser::getCreateTime, nowDate).count();
        if (addNumDay == null) {
            addNumDay = 0;
        }
        userLoginDayService.saveDay(activeNumDay, addNumDay, yesterday);
        if (nowDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            LocalDate beforeWeekMonday = nowDate.minusDays(7);
            Integer activeNumWeek = userLoginService.countByUserAddress(beforeWeekMonday, nowDate);
            if (activeNumWeek == null) {
                activeNumWeek = 0;
            }
            Integer addNumWeek = uUserService.lambdaQuery().ge(UUser::getCreateTime, beforeWeekMonday).lt(UUser::getCreateTime, nowDate).count();
            if (addNumWeek == null) {
                addNumWeek = 0;
            }
            userLoginWeekService.saveWeek(activeNumWeek, addNumWeek, beforeWeekMonday, nowDate);
        }
        LocalDate firstDayOfMonth = nowDate.withDayOfMonth(1);
        if (firstDayOfMonth.compareTo(nowDate) == 0) {
            LocalDate beforeMonthStart = firstDayOfMonth.minusMonths(1);
            Integer activeNumMonth = userLoginService.countByUserAddress(beforeMonthStart, nowDate);
            if (activeNumMonth == null) {
                activeNumMonth = 0;
            }
            Integer addNumMonth = uUserService.lambdaQuery().ge(UUser::getCreateTime, beforeMonthStart).lt(UUser::getCreateTime, nowDate).count();
            if (addNumMonth == null) {
                addNumMonth = 0;
            }
            userLoginMonthService.saveMonth(activeNumMonth, addNumMonth, beforeMonthStart, nowDate);
        }
    }

}
