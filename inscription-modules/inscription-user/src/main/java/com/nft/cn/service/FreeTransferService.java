package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.FreeTransfer;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.FreeTransferReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.FreeTransferListResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface FreeTransferService extends IService<FreeTransfer> {


    BaseResult<String> transfer(FreeTransferReq freeTransferReq);

    PageRespVO<FreeTransferListResp> freeTransferList(PageReqVO pageReqVO);

}
