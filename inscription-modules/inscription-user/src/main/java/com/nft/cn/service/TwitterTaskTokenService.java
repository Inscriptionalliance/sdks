package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.TwitterTaskToken;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.TaskCheckFulfilReq;
import com.nft.cn.vo.resp.CommunityLikeDataResp;
import com.nft.cn.vo.resp.TwitterTaskListResp;

import java.util.List;

public interface TwitterTaskTokenService extends IService<TwitterTaskToken> {

    BaseResult<List<TwitterTaskListResp>> getTwitterTaskListResp();


    BaseResult<CommunityLikeDataResp> communityLikeData();

}
