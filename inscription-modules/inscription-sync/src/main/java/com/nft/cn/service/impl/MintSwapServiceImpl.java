package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.RedisConstant;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.MintSwapMapper;
import com.nft.cn.entity.MintDeploy;
import com.nft.cn.entity.MintHangSale;
import com.nft.cn.entity.MintSwap;
import com.nft.cn.entity.MintUserHold;
import com.nft.cn.service.*;
import com.nft.cn.util.*;
import com.nft.cn.vo.DataSyncVO;
import com.nft.cn.vo.resp.MintSwapSwapResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
public class MintSwapServiceImpl extends ServiceImpl<MintSwapMapper, MintSwap> implements MintSwapService {

    @Autowired
    private NonceSwapService nonceSwapService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private SSystemStatisticsService sSystemStatisticsService;
    @Autowired
    private MintUserHoldService mintUserHoldService;

    @Value("${rpcUrl}")
    private String rpcUrl;

    @Override
    public void swap(DataSyncVO dataSyncVO, String hash, Long blockNum, String swapContractAddress) {
        String payUserAddress = HtRPCApiUtils.getridof_zero_address(NumberUtil.getData(dataSyncVO.getData(), 0, 64));
        BigDecimal totalPrice = new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 128, 192))).divide(new BigDecimal("10").pow(18), 8, RoundingMode.DOWN);
        long type = Long.parseLong(new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 192, 256))).toString());
        Long dataId = Long.valueOf(new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 256, 320))).toString());
        if (type == 1) {
            swapUSDA(payUserAddress, dataId, hash);
        } else if (type == 0) {
            swapMint(payUserAddress, dataId, hash);
        }
    }

    public void swapUSDA(String payUserAddress, Long dataId, String hash) {
        MintSwap mintSwap = getById(dataId);
        mintSwap.setStatus(1);
        mintSwap.setContractHash(hash);
        mintSwap.setUpdateTime(LocalDateTime.now());
        updateById(mintSwap);
        boolean flag = mintUserHoldService.updateMintNum(payUserAddress, mintSwap.getDeployId(), mintSwap.getMintNum(), 2);
        nonceSwapService.updateNonce(payUserAddress);
        sSystemStatisticsService.updateStatistics(SystemStatisticsConstant.mint_price_pool_mint_num, mintSwap.getMintNum(), 1);
        sSystemStatisticsService.updateStatistics(SystemStatisticsConstant.mint_price_pool_usda_num, mintSwap.getAmount(), 2);
        if (redisUtil.getString(RedisConstant.USER_SWAP_TRANSFER_LOCK + payUserAddress) != null) {
            redisUtil.delete(RedisConstant.USER_SWAP_TRANSFER_LOCK + payUserAddress );
        }
    }

    public void swapMint(String payUserAddress, Long dataId, String hash) {
        MintSwap mintSwap = getById(dataId);
        mintSwap.setContractHash(hash);
        mintSwap.setStatus(1);
        mintSwap.setUpdateTime(LocalDateTime.now());
        updateById(mintSwap);
        String mintJson = "data:,{\"p\":\"denim-20\",\"op\":\"transfer\",\"tick\":\"btia\",\"amt\":\"" + mintSwap.getMintNum().setScale(0, RoundingMode.DOWN).toPlainString() + "\"}" + mintSwap.getId();
        String privateKey = "aaaaa";
        sendJson(privateKey, payUserAddress, Hex.stringToHex(mintJson));
        nonceSwapService.updateNonce(payUserAddress);
        sSystemStatisticsService.updateStatistics(SystemStatisticsConstant.mint_price_pool_mint_num, mintSwap.getMintNum(), 2);
        sSystemStatisticsService.updateStatistics(SystemStatisticsConstant.mint_price_pool_usda_num, mintSwap.getAmount(), 1);
        if (redisUtil.getString(RedisConstant.USER_SWAP_TRANSFER_LOCK + payUserAddress) != null) {
            redisUtil.delete(RedisConstant.USER_SWAP_TRANSFER_LOCK + payUserAddress );
        }
    }


    public void sendJson(String privateKey, String toAddress, String data) {
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        Credentials credentials = Credentials.create(privateKey);
        BigInteger value = BigInteger.valueOf(0);
        Request<?, EthGetTransactionCount> nonceRequest = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST);
        try {
            EthGetTransactionCount nonceResponse = nonceRequest.send();
            if (nonceResponse.hasError()) {
                return;
            }
            BigInteger nonce = nonceResponse.getTransactionCount();
            RawTransaction transaction = RawTransaction.createTransaction(
                    nonce,
                    new BigInteger("5000000000"),
                    new BigInteger("48000"),
                    toAddress,
                    value,
                    data
            );
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(transaction, credentials))).send();
            String transactionHash = ethSendTransaction.getTransactionHash();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BigDecimal getPrice(BigInteger mintNum, Integer type) {
        String mintPricePoolCoefficient = sSystemConfigService.getByKey(SystemConfigConstant.mint_price_pool_coefficient).getConfigValue();
        BigDecimal coefficient = new BigDecimal(mintPricePoolCoefficient);
        BigDecimal poolMintNum = new BigDecimal(sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_price_pool_mint_num).getStatisticsValue());
        BigDecimal poolUsdaNum = new BigDecimal(sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_price_pool_usda_num).getStatisticsValue());
        BigDecimal price = null;
        if (type == 1) {
            price = poolUsdaNum.subtract(coefficient.divide(poolMintNum.add(new BigDecimal(mintNum)), 18, RoundingMode.DOWN));
        } else if (type == 0) {
            price = coefficient.divide(poolMintNum.subtract(new BigDecimal(mintNum)), 18, RoundingMode.DOWN).subtract(poolUsdaNum);
        }
        return price;
    }
}
