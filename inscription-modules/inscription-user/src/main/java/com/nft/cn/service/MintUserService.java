package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintDeploy;
import com.nft.cn.entity.MintUser;
import com.nft.cn.entity.UUser;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.*;
import lombok.Synchronized;

import java.math.BigDecimal;

public interface MintUserService extends IService<MintUser> {

    BaseResult<MintMintResp> mint();

    MintUser saveMintUser(UUser uUser, MintDeploy mintDeploy, BigDecimal mintTicket, BigDecimal freeTicket);

    String getEnStr(MintUser mintUser);

    PageRespVO<MintMintListResp> mintList(PageReqVO pageReqVO);

    PageRespVO<MintMintRankListResp> mintRankList(PageReqVO pageReqVO);

    BaseResult<MintPayBoxResp> payBox(MintMintReq mintMintReq);

    BaseResult<String> mintAuth(MintMintAuthReq mintMintAuthReq);

    BaseResult<String> payBoxAuth(MintPayAuthReq mintPayAuthReq);

    BaseResult<String> mintAuthError(MintMintAuthErrorReq mintMintAuthErrorReq);

    BaseResult<String> payBoxAuthError(MintPayAuthErrorReq mintPayAuthErrorReq);

    BaseResult<String> sendJson(String privateKey, String toAddress, String data);

    BaseResult<MintStatusResp> mintStatus();

    PageRespVO<MintMintUserListResp> mintListUser(PageReqVO pageReqVO);

    BigDecimal selectAchieve(Long userId);

}
