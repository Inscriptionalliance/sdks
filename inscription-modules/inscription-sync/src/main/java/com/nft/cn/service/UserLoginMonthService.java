package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UserLoginMonth;

import java.time.LocalDate;

public interface UserLoginMonthService extends IService<UserLoginMonth> {

    void saveMonth(Integer activeNumMonth, Integer addNumMonth, LocalDate beforeMonthStart, LocalDate nowDate);

}
