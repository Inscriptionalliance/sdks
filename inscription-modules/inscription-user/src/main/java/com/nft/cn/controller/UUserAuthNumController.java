package com.nft.cn.controller;

import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.dao.UUserAuthNumMapper;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserAuthNum;
import com.nft.cn.service.UUserAuthNumService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.UUID;
import com.nft.cn.vo.req.AuthStatusListReq;
import com.nft.cn.vo.resp.AuthStatusListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/uUserAuthNum")
public class UUserAuthNumController {

    @Autowired
    private UUserAuthNumService uUserAuthNumService;

    @UserLoginToken
    @PostMapping(value = "/authStatusList")
    public BaseResult<AuthStatusListResp> authStatusList(@RequestBody AuthStatusListReq authStatusListReq){
        return uUserAuthNumService.authStatusList(authStatusListReq);
    }


}
