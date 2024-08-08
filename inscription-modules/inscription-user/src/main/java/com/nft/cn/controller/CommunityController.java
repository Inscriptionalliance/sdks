package com.nft.cn.controller;


import com.nft.cn.annotation.PassToken;
import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.CommunityService;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.CommunityLikeReq;
import com.nft.cn.vo.req.CommunityListReq;
import com.nft.cn.vo.req.FreeTransferReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.CommunityListResp;
import com.nft.cn.vo.resp.FreeTransferListResp;
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
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;


    @PassToken
    @PostMapping(value = "/communityList/passToken")
    public BaseResult<PageRespVO<CommunityListResp>> communityListPassToken(@RequestBody CommunityListReq communityListReq){
        return BaseResult.success(communityService.communityListPassToken(communityListReq));
    }

    @UserLoginToken
    @PostMapping(value = "/communityList")
    public BaseResult<PageRespVO<CommunityListResp>> communityList(@RequestBody CommunityListReq communityListReq){
        return BaseResult.success(communityService.communityList(communityListReq));
    }

    @UserLoginToken
    @PostMapping(value = "/like")
    public BaseResult<String> communityLike(@RequestBody CommunityLikeReq communityLikeReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-communityLike." + userByToken.getUserAddress();
        try {
            boolean flag = redisUtils.setNx(key, "mint-communityLike" + userByToken.getUserAddress(), 300);
            if (flag) {
                BaseResult<String> communityLike = communityService.communityLike(communityLikeReq);
                redisUtils.remove(key);
                return communityLike;
            }
            return BaseResult.fail(i18nService.getMessage("10006"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }



}

