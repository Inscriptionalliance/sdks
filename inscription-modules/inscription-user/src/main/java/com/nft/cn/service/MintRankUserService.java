package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintRankUser;
import com.nft.cn.vo.req.MintRankUserReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintRankUserResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface MintRankUserService extends IService<MintRankUser> {

    PageRespVO<MintRankUserResp> mintRankUser(PageReqVO pageReqVO);

    PageRespVO<MintRankUserResp> mintRankUserSelectedRank(MintRankUserReq mintRankUserReq);



}
