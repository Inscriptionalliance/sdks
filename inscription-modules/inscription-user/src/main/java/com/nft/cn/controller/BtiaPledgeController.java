package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.BtiaPledgeService;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.resp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/btiaPledge")
public class BtiaPledgeController {

    @Autowired
    private BtiaPledgeService btiaPledgeService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;


    @UserLoginToken
    @PostMapping(value = "/pledgeStatus")
    public BaseResult<PledgeStatusResp> pledgeStatus(){
        return btiaPledgeService.pledgeStatus();
    }

    @UserLoginToken
    @PostMapping(value = "/pledge")
    public BaseResult<BtiaPledgeResp> pledge(){
        UUser userByToken = uUserService.getTokenUser();
        String key = "btia-pledge." + userByToken.getUserAddress();
        try {
            boolean flag = redisUtils.setNx(key, "btia-pledge" + userByToken.getUserAddress(), 300);
            if (flag) {
                BaseResult<BtiaPledgeResp> transfer = btiaPledgeService.pledge();
                redisUtils.remove(key);
                return transfer;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }

    }


    @UserLoginToken
    @PostMapping(value = "/pledgeInfo")
    public BaseResult<PledgeInfoResp> pledgeInfo(){
        return btiaPledgeService.pledgeInfo();
    }




}

