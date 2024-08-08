package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.MintTransferService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.MintTransferAuthErrorReq;
import com.nft.cn.vo.req.MintTransferAuthReq;
import com.nft.cn.vo.req.MintTransferReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintTransferListResp;
import com.nft.cn.vo.resp.MintTransferResp;
import com.nft.cn.vo.resp.PageRespVO;
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
@RequestMapping("/mintTransfer")
public class MintTransferController {

    @Autowired
    private MintTransferService mintTransferService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;

    @UserLoginToken
    @PostMapping(value = "/mintTransferList")
    public BaseResult<PageRespVO<MintTransferListResp>> mintTransferList(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(mintTransferService.mintTransferList(pageReqVO));
    }

    @UserLoginToken
    @PostMapping(value = "/transfer")
    public BaseResult<MintTransferResp> transfer(@RequestBody MintTransferReq mintTransferReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-transfer." + userByToken.getUserAddress();
        try {
            return BaseResult.fail(i18nService.getMessage("88888"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }


    @UserLoginToken
    @PostMapping(value = "/transferAuth")
    public BaseResult<String> transferAuth(@RequestBody MintTransferAuthReq mintTransferAuthReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "transferAuth." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "transferAuth." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintTransferService.transferAuth(mintTransferAuthReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }

    @UserLoginToken
    @PostMapping(value = "/transferAuthError")
    public BaseResult<String> transferAuthError(@RequestBody MintTransferAuthErrorReq mintTransferAuthErrorReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "transferAuthError." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "transferAuthError." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintTransferService.transferAuthError(mintTransferAuthErrorReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }

}

