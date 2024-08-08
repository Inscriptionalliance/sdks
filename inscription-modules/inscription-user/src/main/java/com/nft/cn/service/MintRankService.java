package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintRank;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintRankCommunityResp;
import com.nft.cn.vo.resp.MintRankNodeResp;
import com.nft.cn.vo.resp.MintRankResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface MintRankService extends IService<MintRank> {

    PageRespVO<MintRankResp> mintRank(PageReqVO pageReqVO);

}
