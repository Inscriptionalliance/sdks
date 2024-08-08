package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UserLogin;

import java.time.LocalDate;

public interface UserLoginService extends IService<UserLogin> {

    void login(UUser user);

    Integer countByUserAddress(LocalDate startDay, LocalDate endDay);

}
