package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UserLoginDay;

import java.time.LocalDate;

public interface UserLoginDayService extends IService<UserLoginDay> {

    void saveDay(Integer activeNumDay, Integer addNumDay, LocalDate yesterday);
}
