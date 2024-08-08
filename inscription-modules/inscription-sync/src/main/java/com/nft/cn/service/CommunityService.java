package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.Community;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.CommunityLikeReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.CommunityListResp;
import com.nft.cn.vo.resp.PageRespVO;

import java.time.LocalDate;

public interface CommunityService extends IService<Community> {

}
