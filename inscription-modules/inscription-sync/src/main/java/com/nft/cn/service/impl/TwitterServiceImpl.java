package com.nft.cn.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.entity.TaskTweet;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserTwitterOauth;
import com.nft.cn.entity.UUserTwitterToken;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TwitterServiceImpl implements TwitterService {


    @Autowired
    private UUserService userService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UUserTwitterOauthService uUserTwitterOauthService;
    @Autowired
    private UUserTwitterTokenService uUserTwitterTokenService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private TaskTweetService taskTweetService;

    @Override
    public void sendTweet(String content, String bearerToken) {


    }

    @Override
    public BaseResult<String> checkOauth2(String userAddress) {
        UUserTwitterToken one = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserAddress, userAddress).one();
        if (one == null) {
            return BaseResult.fail(i18nService.getMessage("20034"));
        }
        LocalDateTime expireTime = one.getExpireTime();
        expireTime = expireTime.minusMinutes(10);
        if (expireTime.compareTo(LocalDateTime.now()) < 0) {
            return BaseResult.fail(i18nService.getMessage("20035"));
        }
        return BaseResult.success();
    }


    @Override
    public BaseResult<String> replaceTwitterToken(UUserTwitterToken uUserTwitterToken) {
        return null;
    }


    @Override
    public BaseResult<String> replaceTweets(String officialTwitterId, UUserTwitterToken uUserTwitterToken, String pageToken) {
        return null;
    }

    @Override
    public BaseResult<String> checkRetweets(String tweetId, UUserTwitterToken uUserTwitterToken, String pageToken) {
        return null;
    }

    @Override
    public BaseResult<String> checkQuoteTweets(String tweetId, UUserTwitterToken uUserTwitterToken, String pageToken) {
        return null;
    }

    @Override
    public BaseResult<String> checkLiking(String tweetId, UUserTwitterToken uUserTwitterToken, String pageToken) {
        return null;
    }

    @Override
    public BaseResult<String> checkFollows(String twitterId, UUserTwitterToken uUserTwitterToken) {
        return null;
    }




}