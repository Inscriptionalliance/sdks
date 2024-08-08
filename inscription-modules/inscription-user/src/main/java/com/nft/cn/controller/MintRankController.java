package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.service.MintRankCommunityService;
import com.nft.cn.service.MintRankNodeService;
import com.nft.cn.service.MintRankService;
import com.nft.cn.service.MintRankUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.MintRankCommunityReq;
import com.nft.cn.vo.req.MintRankNodeReq;
import com.nft.cn.vo.req.MintRankUserReq;
import com.nft.cn.vo.req.PageReqVO;
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
@RequestMapping("/mintRank")
public class MintRankController {

    @Autowired
    private MintRankService mintRankService;
    @Autowired
    private MintRankCommunityService mintRankCommunityService;
    @Autowired
    private MintRankNodeService mintRankNodeService;
    @Autowired
    private MintRankUserService mintRankUserService;

    @UserLoginToken
    @PostMapping(value = "/mintRank")
    public BaseResult<PageRespVO<MintRankResp>> mintRank(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(mintRankService.mintRank(pageReqVO));
    }

    @UserLoginToken
    @PostMapping(value = "/mintRankCommunity")
    public BaseResult<PageRespVO<MintRankCommunityResp>> mintRankCommunity(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(mintRankCommunityService.mintRankCommunity(pageReqVO));
    }

    @UserLoginToken
    @PostMapping(value = "/mintRankUser")
    public BaseResult<PageRespVO<MintRankUserResp>> mintRankUser(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(mintRankUserService.mintRankUser(pageReqVO));
    }


    @UserLoginToken
    @PostMapping(value = "/mintRankCommunitySelectedRank")
    public BaseResult<PageRespVO<MintRankCommunityResp>> mintRankCommunitySelectedRank(@RequestBody MintRankCommunityReq mintRankCommunityReq){
        return BaseResult.success(mintRankCommunityService.mintRankCommunitySelectedRank(mintRankCommunityReq));
    }

    @UserLoginToken
    @PostMapping(value = "/mintRankUserSelectedRank")
    public BaseResult<PageRespVO<MintRankUserResp>> mintRankUserSelectedRank(@RequestBody MintRankUserReq mintRankUserReq){
        return BaseResult.success(mintRankUserService.mintRankUserSelectedRank(mintRankUserReq));
    }

    @UserLoginToken
    @PostMapping(value = "/mintRankPhase")
    public BaseResult<MintRankPhaseResp> mintRankPhase(){
        return BaseResult.success(mintRankCommunityService.mintRankPhase());
    }

}

