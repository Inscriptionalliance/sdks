package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.service.MintDeployService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.LotteryDrawTypeInfoReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.FreeTransferListResp;
import com.nft.cn.vo.resp.LotteryDrawTypeInfoResp;
import com.nft.cn.vo.resp.MintDeployInfoResp;
import com.nft.cn.vo.resp.PageRespVO;
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
@RequestMapping("/mintDeploy")
public class MintDeployController {

    @Autowired
    private MintDeployService mintDeployService;

    @UserLoginToken
    @PostMapping(value = "/info")
    public BaseResult<MintDeployInfoResp> mintDeployInfo(){
        return mintDeployService.mintDeployInfo();
    }


}

