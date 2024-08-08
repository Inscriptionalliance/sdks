package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.WhitePay;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.WhitePayListResp;

public interface WhitePayService extends IService<WhitePay> {

    PageRespVO<WhitePayListResp> whitePayList(PageReqVO pageReqVO);

}
