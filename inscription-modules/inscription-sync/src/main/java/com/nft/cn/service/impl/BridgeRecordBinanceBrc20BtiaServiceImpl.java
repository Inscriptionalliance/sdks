package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.RedisConstant;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.BridgeRecordBinanceBrc20BtiaMapper;
import com.nft.cn.entity.BridgeRecordBinanceBrc20Btia;
import com.nft.cn.entity.MintSwap;
import com.nft.cn.service.*;
import com.nft.cn.util.Hex;
import com.nft.cn.util.HtRPCApiUtils;
import com.nft.cn.util.NumberUtil;
import com.nft.cn.util.RedisUtil;
import com.nft.cn.vo.DataSyncVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
public class BridgeRecordBinanceBrc20BtiaServiceImpl extends ServiceImpl<BridgeRecordBinanceBrc20BtiaMapper, BridgeRecordBinanceBrc20Btia> implements BridgeRecordBinanceBrc20BtiaService {

    @Autowired
    private NonceBridgeService nonceBridgeService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BridgeHoldBinanceBrc20BtiaService bridgeHoldBinanceBrc20BtiaService;

    @Override
    public void bridge(DataSyncVO dataSyncVO, String hash, Long blockNum, String bridgeContractAddress) {
        String payUserAddress = HtRPCApiUtils.getridof_zero_address(NumberUtil.getData(dataSyncVO.getData(), 0, 64));
        String protocol = Hex.hexToString(NumberUtil.getData(dataSyncVO.getData(), 64, 128));
        String tick = Hex.hexToString(NumberUtil.getData(dataSyncVO.getData(), 128, 192));
        BigDecimal amountIn = new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 192, 256))).divide(new BigDecimal("10").pow(18), 8, RoundingMode.DOWN);
        BigDecimal amountOut = new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 256, 320))).divide(new BigDecimal("10").pow(18), 8, RoundingMode.DOWN);
        Long dataId = Long.valueOf(new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 320, 384))).toString());
        BridgeRecordBinanceBrc20Btia byId = getById(dataId);
        byId.setStatus(1);
        byId.setContractHash(hash);
        byId.setUpdateTime(LocalDateTime.now());
        updateById(byId);
        boolean flag = bridgeHoldBinanceBrc20BtiaService.updateBalance(payUserAddress, amountOut, 1);
        nonceBridgeService.updateNonce(payUserAddress);
        if (redisUtil.getString(RedisConstant.USER_BRIDGE_TRANSFER_LOCK + payUserAddress) != null) {
            redisUtil.delete(RedisConstant.USER_BRIDGE_TRANSFER_LOCK + payUserAddress );
        }
    }
}
