package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.LotteryDrawPrizeService;
import com.nft.cn.service.LotteryDrawTypeService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.LotteryDrawReq;
import com.nft.cn.vo.req.LotteryDrawTypeInfoReq;
import com.nft.cn.vo.resp.LotteryDrawResp;
import com.nft.cn.vo.resp.LotteryDrawTypeInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/lotteryDraw")
public class LotteryDrawController {

    @Autowired
    private LotteryDrawTypeService lotteryDrawTypeService;
    @Autowired
    private LotteryDrawPrizeService lotteryDrawPrizeService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private RedisUtils redisUtils;

    @UserLoginToken
    @PostMapping(value = "/typeInfo")
    public BaseResult<LotteryDrawTypeInfoResp> lotteryDrawTypeInfo(@RequestBody LotteryDrawTypeInfoReq lotteryDrawTypeInfoReq){
        return lotteryDrawTypeService.lotteryDrawTypeInfo(lotteryDrawTypeInfoReq);
    }

    @UserLoginToken
    @PostMapping(value = "/lotteryDraw")
    public BaseResult<LotteryDrawResp> lotteryDraw(@RequestBody LotteryDrawReq lotteryDrawReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-lotteryDraw." + userByToken.getUserAddress();
        try {
            boolean flag = redisUtils.setNx(key, "mint-lotteryDraw" + userByToken.getUserAddress(), 300);
            if (flag) {
                BaseResult<LotteryDrawResp> lotteryDrawRespBaseResult = lotteryDrawPrizeService.lotteryDraw(lotteryDrawReq);
                redisUtils.remove(key);
                return lotteryDrawRespBaseResult;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }

}

