package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UserLoginWeek;

import java.time.LocalDate;

public interface UserLoginWeekService extends IService<UserLoginWeek> {

    void saveWeek(Integer activeNumWeek, Integer addNumWeek, LocalDate beforeWeekMonday, LocalDate nowDate);

}
