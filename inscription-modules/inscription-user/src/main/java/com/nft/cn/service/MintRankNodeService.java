package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintRankNode;
import com.nft.cn.vo.req.MintRankNodeReq;
import com.nft.cn.vo.req.MintRankUserReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintRankNodeResp;
import com.nft.cn.vo.resp.MintRankUserResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface MintRankNodeService extends IService<MintRankNode> {

    PageRespVO<MintRankNodeResp> mintRankNode(PageReqVO pageReqVO);

    PageRespVO<MintRankNodeResp> mintRankNodeSelectedRank(MintRankNodeReq mintRankNodeReq);


}
