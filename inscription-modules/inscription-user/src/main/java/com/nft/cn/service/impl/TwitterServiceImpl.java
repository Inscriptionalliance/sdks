package com.nft.cn.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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


    @Override
    public String oauth2Url(Long userId) {
        return "";
    }

    @Override
    public String oauth2Url() {
        UUser tokenUser = userService.getTokenUser();
        return oauth2Url(tokenUser.getId());
    }

    @Override
    public BaseResult<String> oauth2CallBack(HttpServletRequest request) {
        return null;
    }

    @Override
    public void sendTweet(String content, String bearerToken) {

    }

    @Override
    public BaseResult<String> checkOauth2() {
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