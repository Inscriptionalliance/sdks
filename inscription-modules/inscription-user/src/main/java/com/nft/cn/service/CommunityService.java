package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.Community;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.CommunityLikeReq;
import com.nft.cn.vo.req.CommunityListReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.CommunityListResp;
import com.nft.cn.vo.resp.PageRespVO;

import java.time.LocalDate;

public interface CommunityService extends IService<Community> {

    PageRespVO<CommunityListResp> communityListPassToken(CommunityListReq communityListReq);

    PageRespVO<CommunityListResp> communityList(CommunityListReq communityListReq);

    BaseResult<String> communityLike(CommunityLikeReq communityLikeReq);

    void updateLikeNum(Long id);

    void updatePartakeNum(Long id);

    boolean isWeekLikeIncome (String userAddress, LocalDate now);


}
