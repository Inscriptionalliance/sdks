package com.nft.cn.controller;


import com.nft.cn.annotation.UserLoginToken;
import com.nft.cn.service.UUserBtcService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.BindBtcReq;
import com.nft.cn.vo.req.BtcSetDefaultReq;
import com.nft.cn.vo.req.UnbindBtcReq;
import com.nft.cn.vo.resp.BtcBindListResp;
import com.nft.cn.vo.resp.GetPinNumResp;
import com.nft.cn.vo.resp.PageRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/uUserBtc")
public class UUserBtcController {

    @Autowired
    private UUserBtcService uUserBtcService;

    @UserLoginToken
    @PostMapping(value = "/getPinNum")
    public BaseResult<GetPinNumResp> getPinNum(){
        return uUserBtcService.getPinNum();
    }

    @UserLoginToken
    @PostMapping(value = "/bindBtc")
    public BaseResult<String> bindBtc(BindBtcReq bindBtcReq){
        return uUserBtcService.bindBtc(bindBtcReq);
    }

    @UserLoginToken
    @PostMapping(value = "/unbindBtc")
    public BaseResult<String> unbindBtc(UnbindBtcReq unbindBtcReq){
        return uUserBtcService.unbindBtc(unbindBtcReq);
    }

    @UserLoginToken
    @PostMapping(value = "/setDefault")
    public BaseResult<String> setDefault(BtcSetDefaultReq btcSetDefaultReq){
        return uUserBtcService.setDefault(btcSetDefaultReq);
    }

    @UserLoginToken
    @PostMapping(value = "/bindList")
    public BaseResult<PageRespVO<BtcBindListResp>> bindList(BtcSetDefaultReq btcSetDefaultReq){
        return uUserBtcService.bindList(btcSetDefaultReq);
    }

}

