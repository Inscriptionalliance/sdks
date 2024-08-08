package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.service.UUserRefereeService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.LastRefereeResp;
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
@RequestMapping("/uUserReferee")
public class UUserRefereeController {

    @Autowired
    private UUserRefereeService uUserRefereeService;

    @UserLoginToken
    @PostMapping(value = "/lastReferee")
    public BaseResult<PageRespVO<LastRefereeResp>> lastReferee(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(uUserRefereeService.lastReferee(pageReqVO));
    }

}

