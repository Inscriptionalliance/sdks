package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UserLoginMapper;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UserLogin;
import com.nft.cn.service.UserLoginService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements UserLoginService {

    @Override
    public void login(UUser user) {
        UserLogin one = lambdaQuery().eq(UserLogin::getUserAddress, user.getUserAddress()).ge(UserLogin::getCreateTime, LocalDate.now()).last("limit 1").one();
        if (one != null) {
            one.setLoginCount(one.getLoginCount() + 1);
            one.setUpdateTime(LocalDateTime.now());
            updateById(one);
        } else {
            one = new UserLogin();
            one.setUserId(user.getId());
            one.setUserAddress(user.getUserAddress());
            one.setLoginCount(1);
            one.setCreateTime(LocalDateTime.now());
            one.setUpdateTime(LocalDateTime.now());
            save(one);
        }
    }
}
