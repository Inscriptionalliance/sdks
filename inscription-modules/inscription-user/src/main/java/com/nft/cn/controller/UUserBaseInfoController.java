package com.nft.cn.controller;


import com.nft.cn.annotation.PassToken;
import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.UUserBaseInfoService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.resp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/uUserBaseInfo")
public class UUserBaseInfoController {

    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;

    @UserLoginToken
    @PostMapping(value = "/checkBindTwitter")
    public BaseResult<String> checkBindTwitter(){
        return uUserBaseInfoService.checkBindTwitter();
    }


    @UserLoginToken
    @PostMapping(value = "/checkFollowsTwitter")
    public BaseResult<String> checkFollowsTwitter(){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-checkFollowsTwitter." + userByToken.getUserAddress();
        try {
            boolean flag = redisUtils.setNx(key, "mint-checkFollowsTwitter" + userByToken.getUserAddress(), 300);
            if (flag) {
                BaseResult<String> stringBaseResult = uUserBaseInfoService.checkFollowsTwitter();
                redisUtils.remove(key);
                return stringBaseResult;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }

    @UserLoginToken
    @PostMapping(value = "/interestTwitterInfo")
    public BaseResult<InterestTwitterInfoResp> interestTwitterInfo(){
        return uUserBaseInfoService.interestTwitterInfo();
    }

    @UserLoginToken
    @PostMapping(value = "/checkRequirement")
    public BaseResult<String> checkRequirement(){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-checkRequirement." + userByToken.getUserAddress();
        try {
            boolean flag = redisUtils.setNx(key, "mint-checkRequirement" + userByToken.getUserAddress(), 3000);
            if (flag) {
                BaseResult<String> stringBaseResult = uUserBaseInfoService.checkRequirement();
                redisUtils.remove(key);
                return stringBaseResult;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }

    @UserLoginToken
    @PostMapping(value = "/joinSystem")
    public BaseResult<String> joinSystem(){
        return uUserBaseInfoService.joinSystem();
    }

    @UserLoginToken
    @PostMapping(value = "/teamInfo")
    public BaseResult<TeamInfoResp> teamInfo(){
        return BaseResult.success(uUserBaseInfoService.teamInfo());
    }

    @PassToken
    @PostMapping(value = "/bottomData")
    public BaseResult<BottomDataResp> bottomData(){
        return BaseResult.success(uUserBaseInfoService.bottomData());
    }


    @PassToken
    @PostMapping(value = "/loginIncome")
    public BaseResult<LoginIncomeResp> loginIncome(){
        return BaseResult.success(uUserBaseInfoService.loginIncome());
    }



}

