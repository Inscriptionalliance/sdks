package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.FreeTransferReq;
import com.nft.cn.vo.req.LotteryDrawTypeInfoReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.FreeTransferListResp;
import com.nft.cn.vo.resp.LotteryDrawTypeInfoResp;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.WhitePayListResp;
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
@RequestMapping("/freeTransfer")
public class FreeTransferController {

    @Autowired
    private FreeTransferService freeTransferService;
    @Autowired
    private TwitterTaskTokenService twitterTaskTokenService;
    @Autowired
    private TwitterTaskService twitterTaskService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtils redisUtils;


    @UserLoginToken
    @PostMapping(value = "/transfer")
    public BaseResult<String> transfer(@RequestBody FreeTransferReq freeTransferReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "free-transfer." + userByToken.getUserAddress();
        try {
            boolean flag = redisUtils.setNx(key, "free-transfer" + userByToken.getUserAddress(), 300);
            if (flag) {
                BaseResult<String> transfer = freeTransferService.transfer(freeTransferReq);
                redisUtils.remove(key);
                return transfer;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }


    @UserLoginToken
    @PostMapping(value = "/freeTransferList")
    public BaseResult<PageRespVO<FreeTransferListResp>> freeTransferList(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(freeTransferService.freeTransferList(pageReqVO));
    }



}

