package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.BridgeRecordBinanceBrc20Btia;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.MintUserBoxBridgePayReq;
import com.nft.cn.vo.req.MintUserBoxBridgeReq;
import com.nft.cn.vo.resp.MintUserBoxBridgePayResp;
import com.nft.cn.vo.resp.MintUserBoxBridgeResp;

public interface BridgeRecordBinanceBrc20BtiaService extends IService<BridgeRecordBinanceBrc20Btia> {

    BaseResult<MintUserBoxBridgeResp> btiaBridge(MintUserBoxBridgeReq mintUserBoxBridgeReq);

    BaseResult<MintUserBoxBridgePayResp> btiaBridgePay(MintUserBoxBridgePayReq mintUserBoxBridgePayReq);

}
