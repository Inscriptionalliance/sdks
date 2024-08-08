package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintUser;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.MintMintAuthErrorReq;
import com.nft.cn.vo.req.MintMintReq;
import com.nft.cn.vo.req.MintPayAuthErrorReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.*;

public interface MintUserService extends IService<MintUser> {


    BaseResult<String> mintAuthError(String userAddress, MintMintAuthErrorReq mintMintAuthErrorReq);

    BaseResult<String> payBoxAuthError(String userAddress, MintPayAuthErrorReq mintPayAuthErrorReq);

}
