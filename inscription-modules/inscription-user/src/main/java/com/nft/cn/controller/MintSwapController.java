package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.MintSwapService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.vo.req.MintSwapGetPriceReq;
import com.nft.cn.vo.req.MintSwapSwapReq;
import com.nft.cn.vo.resp.MintSwapDrawSwapUsdaResp;
import com.nft.cn.vo.resp.MintSwapGetPriceResp;
import com.nft.cn.vo.resp.MintSwapInfoResp;
import com.nft.cn.vo.resp.MintSwapSwapResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mintSwap")
public class MintSwapController {

    @Autowired
    private MintSwapService mintSwapService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private I18nService i18nService;

    @UserLoginToken
    @PostMapping(value = "/info")
    public BaseResult<MintSwapInfoResp> info(){
        return mintSwapService.info();
    }


    @UserLoginToken
    @PostMapping(value = "/swap")
    public BaseResult<MintSwapSwapResp> swap(@RequestBody MintSwapSwapReq mintSwapSwapReq){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-swap." + userByToken.getUserAddress();
        try {
            return BaseResult.fail(i18nService.getMessage("88888"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }

    @UserLoginToken
    @PostMapping(value = "/drawSwapUsda")
    public BaseResult<MintSwapDrawSwapUsdaResp> drawSwapUsda(){
        UUser userByToken = uUserService.getTokenUser();
        String key = "mint-drawSwapUsda." + userByToken.getUserAddress();
        try {
            return BaseResult.fail(i18nService.getMessage("88888"));
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("10006"));
        }
    }


    @UserLoginToken
    @PostMapping(value = "/getPrice")
    public BaseResult<MintSwapGetPriceResp> getPrice(@RequestBody MintSwapGetPriceReq mintSwapGetPriceReq){
        return mintSwapService.getPrice(mintSwapGetPriceReq);
    }


}

