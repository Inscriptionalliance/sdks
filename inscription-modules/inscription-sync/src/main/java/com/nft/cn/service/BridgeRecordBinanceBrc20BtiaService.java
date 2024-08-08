package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.BridgeRecordBinanceBrc20Btia;
import com.nft.cn.vo.DataSyncVO;

public interface BridgeRecordBinanceBrc20BtiaService extends IService<BridgeRecordBinanceBrc20Btia> {

    void bridge(DataSyncVO dataSyncVO, String hash, Long blockNum, String bridgeContractAddress);

}
