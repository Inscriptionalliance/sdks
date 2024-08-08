package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintHangSale;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.*;

public interface MintHangSaleService extends IService<MintHangSale> {

    PageRespVO<MintHangSaleListResp> hangSaleList(MintHangSaleListReq mintHangSaleListReq);

    MyMintResp myMint();

    BaseResult<MintHangSaleResp> hangSale(MintHangSaleReq mintHangSaleReq);

    BaseResult<MintWithdrawResp> withdraw(MintWithdrawReq mintWithdrawReq);

    BaseResult<MintPayResp> pay(MintPayReq mintPayReq);

    BaseResult<String> withdrawAuth(MintAuthReq mintAuthReq);

    BaseResult<String> withdrawAuthError(MintAuthReq mintAuthReq);

    BaseResult<String> payAuth(MintAuthReq mintAuthReq);

    BaseResult<String> payAuthError(MintAuthReq mintAuthReq);

}
