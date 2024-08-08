package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.service.VipUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.resp.MintMinAreaResp;
import com.nft.cn.vo.resp.TeamInfoResp;
import com.nft.cn.vo.resp.VipUserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/vipUser")
public class VipUserController {

    @Autowired
    private VipUserService vipUserService;

    @UserLoginToken
    @PostMapping(value = "/info")
    public BaseResult<VipUserInfoResp> info(){
        return BaseResult.success(vipUserService.info());
    }


    @UserLoginToken
    @PostMapping(value = "/communityList")
    public BaseResult<VipUserInfoResp> communityList(){
        return BaseResult.success(vipUserService.communityList());
    }



    @UserLoginToken
    @PostMapping(value = "/refereeList")
    public BaseResult<VipUserInfoResp> refereeList(){
        return BaseResult.success(vipUserService.refereeList());
    }




}

