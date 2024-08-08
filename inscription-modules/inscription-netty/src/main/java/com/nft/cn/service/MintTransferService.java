package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintTransfer;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.MintTransferAuthErrorReq;
import com.nft.cn.vo.req.MintTransferAuthReq;
import com.nft.cn.vo.req.MintTransferReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintTransferListResp;
import com.nft.cn.vo.resp.MintTransferResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface MintTransferService extends IService<MintTransfer> {

    BaseResult<String> transferAuthError(String userAddress, MintTransferAuthErrorReq mintTransferAuthErrorReq);

}
