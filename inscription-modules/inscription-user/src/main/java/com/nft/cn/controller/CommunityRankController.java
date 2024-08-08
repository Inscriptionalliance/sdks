package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.service.CommunityRankService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.CommunityListResp;
import com.nft.cn.vo.resp.CommunityRankResp;
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
@RequestMapping("/communityRank")
public class CommunityRankController {

    @Autowired
    private CommunityRankService communityRankService;

    @UserLoginToken
    @PostMapping(value = "/communityRank")
    public BaseResult<PageRespVO<CommunityRankResp>> communityRank(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(communityRankService.communityRank(pageReqVO));
    }


}

