package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.service.WhitePayService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.WhitePayListResp;
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
@RequestMapping("/whitePay")
public class WhitePayController {

    @Autowired
    private WhitePayService whitePayService;

    @UserLoginToken
    @PostMapping(value = "/whitePayList")
    public BaseResult<PageRespVO<WhitePayListResp>> whitePayList(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(whitePayService.whitePayList(pageReqVO));
    }



}

