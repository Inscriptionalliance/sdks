package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintRankCommunity;
import com.nft.cn.vo.req.MintRankCommunityReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintRankCommunityResp;
import com.nft.cn.vo.resp.MintRankPhaseResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface MintRankCommunityService extends IService<MintRankCommunity> {

    PageRespVO<MintRankCommunityResp> mintRankCommunity(PageReqVO pageReqVO);

    PageRespVO<MintRankCommunityResp> mintRankCommunitySelectedRank(MintRankCommunityReq mintRankCommunityReq);

    MintRankPhaseResp mintRankPhase();

}
