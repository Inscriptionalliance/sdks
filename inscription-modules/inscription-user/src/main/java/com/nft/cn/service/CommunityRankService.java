package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.CommunityRank;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.CommunityRankResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface CommunityRankService extends IService<CommunityRank> {

    PageRespVO<CommunityRankResp> communityRank(PageReqVO pageReqVO);

}
