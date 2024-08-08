package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.TeamRank;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.TeamRankResp;

public interface TeamRankService extends IService<TeamRank> {

    PageRespVO<TeamRankResp> teamRank(PageReqVO pageReqVO);

}
