package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.TwitterTaskToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.TwitterTaskService;
import com.nft.cn.service.TwitterTaskTokenService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.TaskCheckFulfilReq;
import com.nft.cn.vo.resp.CommunityLikeDataResp;
import com.nft.cn.vo.resp.TwitterTaskListResp;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

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
    @PostMapping(value = "/taskList")
    public BaseResult<List<TwitterTaskListResp>> getTwitterTaskListResp(){
        return twitterTaskTokenService.getTwitterTaskListResp();
    }

    @UserLoginToken
    @PostMapping(value = "/checkFulfil")
    public BaseResult<String> checkFulfil(@RequestBody TaskCheckFulfilReq taskCheckFulfilReq){
        UUser userByToken = uUserService.getTokenUser();
        try {
            String key = "mint-checkFulfil." + userByToken.getUserAddress();
            boolean flag = redisUtils.setNx(key, "mint-checkFulfil" + userByToken.getUserAddress(), 300);
            if (flag) {
                if (taskCheckFulfilReq.getTaskType() == 1) {
                    return BaseResult.success();
                } else if (taskCheckFulfilReq.getTaskType() == 2) {
                    BaseResult<String> stringBaseResult = twitterTaskService.checkTaskFulfil(taskCheckFulfilReq);
                    redisUtils.remove(key);
                    return stringBaseResult;
                } else if (taskCheckFulfilReq.getTaskType() == 3) {
                    return BaseResult.success();
                } else {
                    return BaseResult.fail(i18nService.getMessage("20040"));
                }
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }

    @UserLoginToken
    @PostMapping(value = "/communityLikeData")
    public BaseResult<CommunityLikeDataResp> communityLikeData(){
        return twitterTaskTokenService.communityLikeData();
    }
}

