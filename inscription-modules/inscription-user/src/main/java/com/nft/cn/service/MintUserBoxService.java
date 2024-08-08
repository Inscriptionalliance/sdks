package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintUserBox;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.*;

public interface MintUserBoxService extends IService<MintUserBox> {

    BaseResult<MintUserBoxInfoResp> info();

    PageRespVO<BoxBridgeBalanceListResp> bridgeBalanceList(BoxBridgeBalanceListReq boxBridgeBalanceListReq);

    BaseResult<MintUserBoxBridgeResp> bridge(MintUserBoxBridgeReq mintUserBoxBridgeReq);

    BaseResult<MintUserBoxBridgePayResp> bridgePay(MintUserBoxBridgePayReq mintUserBoxBridgePayReq);

    BaseResult<MintUserBoxBridgePriceResp> getTargetNum(MintUserBoxBridgePriceReq mintUserBoxBridgePriceReq);

    MintUserBox getByBoxNum(String boxNum);

    PageRespVO<BridgeRecordListResp> bridgeRecordList(BridgeRecordListReq bridgeRecordListReq);

}
