package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintTransfer;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.MintTransferListResp;
import com.nft.cn.vo.resp.MintTransferResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface MintTransferService extends IService<MintTransfer> {

    PageRespVO<MintTransferListResp> mintTransferList(PageReqVO pageReqVO);

    BaseResult<MintTransferResp> transfer(MintTransferReq mintTransferReq);

    BaseResult<String> transferAuth(MintTransferAuthReq mintTransferAuthReq);

    BaseResult<String> transferAuthError(MintTransferAuthErrorReq mintTransferAuthErrorReq);

}
