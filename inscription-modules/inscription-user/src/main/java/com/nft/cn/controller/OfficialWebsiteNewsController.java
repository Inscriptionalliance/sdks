package com.nft.cn.controller;


import com.nft.cn.annotation.PassToken;
import com.nft.cn.service.OfficialWebsiteNewsService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.OfficialWebsiteNewsInfoReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.BottomDataResp;
import com.nft.cn.vo.resp.OfficialWebsiteNewsFirstResp;
import com.nft.cn.vo.resp.OfficialWebsiteNewsInfoResp;
import com.nft.cn.vo.resp.OfficialWebsiteNewsResp;
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
@RequestMapping("/officialWebsiteNews")
public class OfficialWebsiteNewsController {

    @Autowired
    private OfficialWebsiteNewsService officialWebsiteNewsService;

    @PassToken
    @PostMapping(value = "/list")
    public BaseResult<OfficialWebsiteNewsResp> officialWebsiteNewsList(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(officialWebsiteNewsService.officialWebsiteNewsList(pageReqVO));
    }

    @PassToken
    @PostMapping(value = "/listHaveFirst")
    public BaseResult<OfficialWebsiteNewsResp> listHaveFirst(@RequestBody PageReqVO pageReqVO){
        return BaseResult.success(officialWebsiteNewsService.listHaveFirst(pageReqVO));
    }

    @PassToken
    @PostMapping(value = "/info")
    public BaseResult<OfficialWebsiteNewsInfoResp> officialWebsiteNewsInfo(@RequestBody OfficialWebsiteNewsInfoReq officialWebsiteNewsInfoReq){
        return BaseResult.success(officialWebsiteNewsService.officialWebsiteNewsInfo(officialWebsiteNewsInfoReq));
    }

    @PassToken
    @PostMapping(value = "/firstImgList")
    public BaseResult<OfficialWebsiteNewsFirstResp> firstImgList(){
        return BaseResult.success(officialWebsiteNewsService.firstImgList());
    }

}

