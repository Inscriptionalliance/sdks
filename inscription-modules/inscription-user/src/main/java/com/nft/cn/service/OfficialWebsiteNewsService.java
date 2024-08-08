package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.OfficialWebsiteNews;
import com.nft.cn.vo.req.OfficialWebsiteNewsInfoReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.OfficialWebsiteNewsFirstResp;
import com.nft.cn.vo.resp.OfficialWebsiteNewsInfoResp;
import com.nft.cn.vo.resp.OfficialWebsiteNewsResp;

public interface OfficialWebsiteNewsService extends IService<OfficialWebsiteNews> {

    OfficialWebsiteNewsResp officialWebsiteNewsList(PageReqVO pageReqVO);

    OfficialWebsiteNewsResp listHaveFirst(PageReqVO pageReqVO);

    OfficialWebsiteNewsInfoResp officialWebsiteNewsInfo(OfficialWebsiteNewsInfoReq officialWebsiteNewsInfoReq);

    OfficialWebsiteNewsFirstResp firstImgList();


}
