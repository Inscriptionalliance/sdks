package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.MintHangSaleService;
import com.nft.cn.service.SSystemConfigService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mintHangSale")
public class MintHangSaleController {

    @Autowired
    private MintHangSaleService mintHangSaleService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SSystemConfigService sSystemConfigService;

    @UserLoginToken
    @PostMapping(value = "/hangSaleList")
    public BaseResult<PageRespVO<MintHangSaleListResp>> hangSaleList(@RequestBody MintHangSaleListReq mintHangSaleListReq){
        return BaseResult.success(mintHangSaleService.hangSaleList(mintHangSaleListReq));
    }

    @UserLoginToken
    @PostMapping(value = "/myMint")
    public BaseResult<MyMintResp> myMint(){
        return BaseResult.success(mintHangSaleService.myMint());
    }

    @UserLoginToken
    @PostMapping(value = "/hangSale")
    public BaseResult<MintHangSaleResp> hangSale(@RequestBody MintHangSaleReq mintHangSaleReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-hangSale." + userByToken.getUserAddress();
        try {
            return BaseResult.fail(i18nService.getMessage("88888"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }


    @UserLoginToken
    @PostMapping(value = "/withdraw")
    public BaseResult<MintWithdrawResp> withdraw(@RequestBody MintWithdrawReq mintWithdrawReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-withdraw." + mintWithdrawReq.getId();
        try {
            return BaseResult.fail(i18nService.getMessage("88888"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }

    @UserLoginToken
    @PostMapping(value = "/pay")
    public BaseResult<MintPayResp> pay(@RequestBody MintPayReq mintPayReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-pay." + mintPayReq.getId();
        try {
            return BaseResult.fail(i18nService.getMessage("88888"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }



    @UserLoginToken
    @PostMapping(value = "/withdrawAuth")
    public BaseResult<String> withdrawAuth(@RequestBody MintAuthReq mintAuthReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "withdrawAuth." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "withdrawAuth." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintHangSaleService.withdrawAuth(mintAuthReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }

    @UserLoginToken
    @PostMapping(value = "/withdrawAuthError")
    public BaseResult<String> withdrawAuthError(@RequestBody MintAuthReq mintAuthReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "withdrawAuthError." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "withdrawAuthError." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintHangSaleService.withdrawAuthError(mintAuthReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }


    @UserLoginToken
    @PostMapping(value = "/payAuth")
    public BaseResult<String> payAuth(@RequestBody MintAuthReq mintAuthReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "payAuth." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "payAuth." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintHangSaleService.payAuth(mintAuthReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }

    @UserLoginToken
    @PostMapping(value = "/payAuthError")
    public BaseResult<String> payAuthError(@RequestBody MintAuthReq mintAuthReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = null;
        try {
            key = "payAuthError." + userByToken.getUserAddress();
            boolean flag = false;
            do {
                flag = redisUtils.setNx(key, "payAuthError." + userByToken.getUserAddress(), 300);
            } while (!flag);
            return mintHangSaleService.payAuthError(mintAuthReq);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        } finally {
            if (key != null) {
                redisUtils.remove(key);
            }
        }
    }

}

