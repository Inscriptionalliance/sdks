package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.service.UUserRefereeNumService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.resp.InvalidRefereeNumListResp;
import com.nft.cn.vo.resp.RefereeNumUserListResp;
import com.nft.cn.vo.resp.ValidRefereeNumListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/uUserRefereeNum")
public class UUserRefereeNumController {

    @Autowired
    private UUserRefereeNumService uUserRefereeNumService;

    @UserLoginToken
    @PostMapping(value = "/validRefereeNumList")
    public BaseResult<List<ValidRefereeNumListResp>> validRefereeNumList(){
        return BaseResult.success(uUserRefereeNumService.validRefereeNumList());
    }

    @UserLoginToken
    @PostMapping(value = "/invalidRefereeNumList")
    public BaseResult<List<InvalidRefereeNumListResp>> invalidRefereeNumList(){
        return BaseResult.success(uUserRefereeNumService.invalidRefereeNumList());
    }

    @UserLoginToken
    @PostMapping(value = "/refereeNumUserList")
    public BaseResult<List<RefereeNumUserListResp>> refereeNumUserList(){
        return BaseResult.success(uUserRefereeNumService.refereeNumUserList());
    }


}

