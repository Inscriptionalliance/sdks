package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.service.TeamRankService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintRankResp;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.TeamRankResp;
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
@RequestMapping("/teamRank")
public class TeamRankController {

    @Autowired
    private TeamRankService teamRankService;

    @UserLoginToken
    @PostMapping(value = "/teamRank")
    public BaseResult<PageRespVO<TeamRankResp>> teamRank(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(teamRankService.teamRank(pageReqVO));
    }


}

