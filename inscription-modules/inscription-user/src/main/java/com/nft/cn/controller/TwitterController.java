package com.nft.cn.controller;

import com.nft.cn.annotation.PassToken;
import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserTwitterToken;
import com.nft.cn.form.LoginPasswordForm;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.TwitterService;
import com.nft.cn.service.UUserService;
import com.nft.cn.service.UUserTwitterTokenService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.UserRefereeNumReq;
import com.nft.cn.vo.resp.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/twitter")
public class TwitterController {

    @Autowired
    private TwitterService twitterService;
    @Autowired
    private UUserTwitterTokenService uUserTwitterTokenService;
    @Autowired
    private UUserService uUserService;


    @UserLoginToken
    @PostMapping(value = "/oauth2Url")
    public BaseResult<String> oauth2Url(){
        return BaseResult.success(twitterService.oauth2Url());
    }

    @PassToken
    @GetMapping(value = "/oauth2CallBack")
    public BaseResult<String> oauth2CallBack(HttpServletRequest request){
        return twitterService.oauth2CallBack(request);
    }

    @UserLoginToken
    @PostMapping(value = "/checkOauth2")
    public BaseResult<String> checkOauth2(){
        return twitterService.checkOauth2();
    }






    @PassToken
    @GetMapping(value = "/checkRetweets")
    public BaseResult<String> checkRetweets(@RequestParam("userId") Long userid, @RequestParam("twitterId") String twitterId){
        UUserTwitterToken one = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserId, userid).one();
        return twitterService.checkRetweets(twitterId, one, null);
    }

    @PassToken
    @GetMapping(value = "/checkQuoteTweets")
    public BaseResult<String> checkQuoteTweets(@RequestParam("userId") Long userid, @RequestParam("twitterId") String twitterId){
        UUserTwitterToken one = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserId, userid).one();
        return twitterService.checkQuoteTweets(twitterId, one, null);
    }

    @PassToken
    @GetMapping(value = "/checkLiking")
    public BaseResult<String> checkLiking(@RequestParam("userId") Long userid, @RequestParam("twitterId") String twitterId){
        UUserTwitterToken one = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserId, userid).one();
        return twitterService.checkLiking(twitterId, one, null);
    }

    @PassToken
    @GetMapping(value = "/checkFollows")
    public BaseResult<String> checkFollows(@RequestParam("userId") Long userid, @RequestParam("twitterId") String twitterId){
        UUserTwitterToken one = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserId, userid).one();
        return twitterService.checkFollows(twitterId, one);
    }



}
