package com.nft.cn.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UUserMapper;
import com.nft.cn.entity.UUser;
import com.nft.cn.exception.UnauthorizedException;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.TokenService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UUserServiceImpl extends ServiceImpl<UUserMapper, UUser> implements UUserService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private I18nService i18nService;


    @Override
    public UUser getTokenUser(String token) {
        try {
            Claims jsonObject = TokenUtil.getTokenUserInfo(token);

            if (null == jsonObject) {
                throw new UnauthorizedException(i18nService.getMessage("10002"));
            }
            UUser uUser = JSON.parseObject(jsonObject.getSubject(), UUser.class);

            Long uUserId = uUser.getId();
            UUser userInfo = getById(uUserId);

            if (!Optional.ofNullable(userInfo).isPresent()){
                throw new UnauthorizedException(i18nService.getMessage("10002"));
            }
            return uUser;
        } catch (Exception e) {
            throw new UnauthorizedException(i18nService.getMessage("10002"));
        }
    }

    @Override
    public UUser getTokenUser() {
        try {
            Claims jsonObject = TokenUtil.getTokenUserInfo();

            if (null == jsonObject) {
                throw new UnauthorizedException(i18nService.getMessage("10002"));
            }
            UUser uUser = JSON.parseObject(jsonObject.getSubject(), UUser.class);

            Long uUserId = uUser.getId();
            UUser userInfo = getById(uUserId);

            if (!Optional.ofNullable(userInfo).isPresent()){
                throw new UnauthorizedException(i18nService.getMessage("10002"));
            }
            return uUser;
        } catch (Exception e) {
            throw new UnauthorizedException(i18nService.getMessage("10002"));
        }
    }

    @Override
    public UUser getByUserAddress(String userAddress) {
        return lambdaQuery().eq(UUser::getUserAddress, userAddress).one();
    }

}
