package com.nft.cn.controller;

import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.form.LoginPasswordForm;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.UserRefereeNumReq;
import com.nft.cn.vo.resp.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UUserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;

    @PostMapping(value = "loginByPass")
    public BaseResult<Map<String, Object>> loginByPass(@RequestBody LoginPasswordForm form) {
        String key = null;
        try {
            key = "login." + form.getUserAddress() + form.getUserAddress();
            boolean flag = false;
            do {
                flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "install_" + form.getUserAddress() + "install");
            } while (!flag);
            stringRedisTemplate.expire(key, 5, TimeUnit.SECONDS);
            return userService.loginByPass(form);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                stringRedisTemplate.delete(key);
            }
        }
    }


    @PostMapping(value = "userRefereeNum")
    public BaseResult<String> userRefereeNum(@RequestBody UserRefereeNumReq userRefereeNumReq) {
        UUser userByToken = userService.getTokenUser();
        try {
            String key = "inscription-userRefereeNum." + userRefereeNumReq.getRefereeNum();
            boolean flag = redisUtils.setNx(key, "inscription-userRefereeNum" + userByToken.getUserAddress(), 150);
            if (flag) {
                BaseResult<String> stringBaseResult = userService.userRefereeNum(userByToken, userRefereeNumReq);
                redisUtils.remove(key);
                return stringBaseResult;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }


    @UserLoginToken
    @PostMapping(value = "/userInfo")
    public BaseResult<UserInfoResp> userInfo(){
        return BaseResult.success(userService.userInfo());
    }


}
