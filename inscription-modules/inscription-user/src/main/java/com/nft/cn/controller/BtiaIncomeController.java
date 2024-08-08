package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.BtiaIncomeService;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.BtcSetDefaultReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.req.PledgeIncomeReceiveReq;
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
@RequestMapping("/btiaIncome")
public class BtiaIncomeController {

    @Autowired
    private BtiaIncomeService btiaIncomeService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;

    @UserLoginToken
    @PostMapping(value = "/pledgeIncomeList")
    public BaseResult<PageRespVO<PledgeIncomeListResp>> pledgeIncomeList(@RequestBody PageReqVO pageReqVO){
        return btiaIncomeService.pledgeIncomeList(pageReqVO);
    }

    @UserLoginToken
    @PostMapping(value = "/receiveIncome")
    public BaseResult<String> receiveIncome(@RequestBody PledgeIncomeReceiveReq pledgeIncomeReceiveReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "btia-pledgereceiveIncome." + userByToken.getUserAddress();
        try {
            boolean flag = redisUtils.setNx(key, "btia-pledgereceiveIncome" + userByToken.getUserAddress(), 300);
            if (flag) {
                BaseResult<String> transfer = btiaIncomeService.receiveIncome(pledgeIncomeReceiveReq);
                redisUtils.remove(key);
                return transfer;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }


}

