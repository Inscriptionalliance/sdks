package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.MintUserBoxService;
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
@RequestMapping("/mintUserBox")
public class MintUserBoxController {

    @Autowired
    private MintUserBoxService mintUserBoxService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;

    @UserLoginToken
    @PostMapping(value = "/info")
    public BaseResult<MintUserBoxInfoResp> info(){
        return mintUserBoxService.info();
    }


    @UserLoginToken
    @PostMapping(value = "/bridgeBalanceList")
    public BaseResult<PageRespVO<BoxBridgeBalanceListResp>> bridgeBalanceList(@RequestBody BoxBridgeBalanceListReq boxBridgeBalanceListReq){
        return BaseResult.success(mintUserBoxService.bridgeBalanceList(boxBridgeBalanceListReq));
    }

    @UserLoginToken
    @PostMapping(value = "/bridge")
    public BaseResult<MintUserBoxBridgeResp> bridge(@RequestBody MintUserBoxBridgeReq mintUserBoxBridgeReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-bridge." + userByToken.getUserAddress();
        try {
            return BaseResult.fail(i18nService.getMessage("88888"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }

    @UserLoginToken
    @PostMapping(value = "/bridgePay")
    public BaseResult<MintUserBoxBridgePayResp> bridgePay(@RequestBody MintUserBoxBridgePayReq mintUserBoxBridgePayReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-bridge." + userByToken.getUserAddress();
        try {
            return BaseResult.fail(i18nService.getMessage("88888"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }


    @UserLoginToken
    @PostMapping(value = "/getTargetNum")
    public BaseResult<MintUserBoxBridgePriceResp> getTargetNum(@RequestBody MintUserBoxBridgePriceReq mintUserBoxBridgePriceReq){
        return mintUserBoxService.getTargetNum(mintUserBoxBridgePriceReq);
    }

    @UserLoginToken
    @PostMapping(value = "/bridgeRecordList")
    public BaseResult<PageRespVO<BridgeRecordListResp>> bridgeBalanceList(@RequestBody BridgeRecordListReq bridgeRecordListReq){
        return BaseResult.success(mintUserBoxService.bridgeRecordList(bridgeRecordListReq));
    }


}

