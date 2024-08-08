package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.MintUserService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mintUser")
public class MintUserController {

    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;

    @UserLoginToken
    @PostMapping(value = "/mint")
    public BaseResult<MintMintResp> mint(){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-mint-" + userByToken.getUserAddress();
        try {
            boolean flag = redisUtils.setNx(key, "mint-mint" + userByToken.getUserAddress(), 300);
            if (flag) {
                BaseResult<MintMintResp> transfer = mintUserService.mint();
                redisUtils.remove(key);
                return transfer;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }

    @UserLoginToken
    @PostMapping(value = "/payBox")
    public BaseResult<MintPayBoxResp> payBox(@RequestBody MintMintReq mintMintReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-mint-" + userByToken.getUserAddress();
        try {
            boolean flag = redisUtils.setNx(key, "mint-mint" + userByToken.getUserAddress(), 300);
            if (flag) {
                BaseResult<MintPayBoxResp> transfer = mintUserService.payBox(mintMintReq);
                redisUtils.remove(key);
                return transfer;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }

    @UserLoginToken
    @PostMapping(value = "/mintStatus")
    public BaseResult<MintStatusResp> mintStatus(){
        return mintUserService.mintStatus();
    }

    @UserLoginToken
    @PostMapping(value = "/mintAuth")
    public BaseResult<String> mintAuth(@RequestBody MintMintAuthReq mintMintAuthReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "mintAuth." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "mintAuth." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintUserService.mintAuth(mintMintAuthReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }

    @UserLoginToken
    @PostMapping(value = "/payBoxAuth")
    public BaseResult<String> payBoxAuth(@RequestBody MintPayAuthReq mintPayAuthReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "payBoxAuth." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "payBoxAuth." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintUserService.payBoxAuth(mintPayAuthReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }

    @UserLoginToken
    @PostMapping(value = "/mintAuthError")
    public BaseResult<String> mintAuthError(@RequestBody MintMintAuthErrorReq mintMintAuthErrorReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "mintAuthError." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "mintAuthError." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintUserService.mintAuthError(mintMintAuthErrorReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }

    @UserLoginToken
    @PostMapping(value = "/payBoxAuthError")
    public BaseResult<String> payBoxAuthError(@RequestBody MintPayAuthErrorReq mintPayAuthErrorReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "payBoxAuthError." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "payBoxAuthError." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintUserService.payBoxAuthError(mintPayAuthErrorReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }


    @UserLoginToken
    @PostMapping(value = "/mintList")
    public BaseResult<PageRespVO<MintMintListResp>> mintList(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(mintUserService.mintList(pageReqVO));
    }

    @UserLoginToken
    @PostMapping(value = "/mintListUser")
    public BaseResult<PageRespVO<MintMintUserListResp>> mintListUser(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(mintUserService.mintListUser(pageReqVO));
    }


    @UserLoginToken
    @PostMapping(value = "/mintRankList")
    public BaseResult<PageRespVO<MintMintRankListResp>> mintRankList(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(mintUserService.mintRankList(pageReqVO));
    }


}

