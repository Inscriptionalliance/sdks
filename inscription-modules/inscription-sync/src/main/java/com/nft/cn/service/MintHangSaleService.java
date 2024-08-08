package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintHangSale;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.DataSyncVO;
import com.nft.cn.vo.req.MintHangSaleListReq;
import com.nft.cn.vo.req.MintHangSaleReq;
import com.nft.cn.vo.req.MintPayReq;
import com.nft.cn.vo.req.MintWithdrawReq;
import com.nft.cn.vo.resp.MintHangSaleListResp;
import com.nft.cn.vo.resp.MyMintResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface MintHangSaleService extends IService<MintHangSale> {

    void pay(DataSyncVO dataSyncVO, String hash, Long blockNum, String marketContractAddress);

    void withdraw(DataSyncVO dataSyncVO, String hash, Long blockNum, String marketContractAddress);
}
