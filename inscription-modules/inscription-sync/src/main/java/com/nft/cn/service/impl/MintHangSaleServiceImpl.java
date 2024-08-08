package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.RedisConstant;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.MintHangSaleMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.*;
import com.nft.cn.vo.DataSyncVO;
import com.nft.cn.vo.req.MintHangSaleListReq;
import com.nft.cn.vo.req.MintHangSaleReq;
import com.nft.cn.vo.req.MintPayReq;
import com.nft.cn.vo.req.MintWithdrawReq;
import com.nft.cn.vo.resp.MintHangSaleListResp;
import com.nft.cn.vo.resp.MyMintResp;
import com.nft.cn.vo.resp.PageRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MintHangSaleServiceImpl extends ServiceImpl<MintHangSaleMapper, MintHangSale> implements MintHangSaleService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private NonceMarketService nonceMarketService;

    @Value("${rpcUrl}")
    private String rpcUrl;

    @Override
    public void pay(DataSyncVO dataSyncVO, String hash, Long blockNum, String marketContractAddress) {
        String payUserAddress = HtRPCApiUtils.getridof_zero_address(NumberUtil.getData(dataSyncVO.getData(), 0, 64));
        String sellerAddress = HtRPCApiUtils.getridof_zero_address(NumberUtil.getData(dataSyncVO.getData(), 64, 128));
        BigDecimal totalPrice = new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 128, 192))).divide(new BigDecimal("10").pow(18), 8, RoundingMode.DOWN);
        Long dataId = Long.valueOf(new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 192, 256))).toString());
        boolean update = lambdaUpdate().set(MintHangSale::getPayAmountHash, hash).set(MintHangSale::getStatus, 4).set(MintHangSale::getPayUserAddress, payUserAddress).set(MintHangSale::getUpdateTime, LocalDateTime.now()).eq(MintHangSale::getId, dataId).update();
        if (!update) {
        }
        MintHangSale mintHangSale = getById(dataId);
        String mintJson = "data:,{\"p\":\"denim-20\",\"op\":\"transfer\",\"tick\":\"btia\",\"amt\":\"" + mintHangSale.getMintNum().setScale(0, RoundingMode.DOWN).toPlainString() + "\"}" + mintHangSale.getId();
        String privateKey = "aaaaa";
        sendJson(privateKey, payUserAddress, Hex.stringToHex(mintJson));
        nonceMarketService.updateNonce(payUserAddress);
        if (redisUtil.getString(RedisConstant.USER_MARKET_TRANSFER_LOCK + payUserAddress) != null) {
            redisUtil.delete(RedisConstant.USER_MARKET_TRANSFER_LOCK + payUserAddress );
        }
    }

    @Override
    public void withdraw(DataSyncVO dataSyncVO, String hash, Long blockNum, String marketContractAddress) {
        String userAddress = HtRPCApiUtils.getridof_zero_address(NumberUtil.getData(dataSyncVO.getData(), 0, 64));
        Long dataId = Long.valueOf(new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 64, 128))).toString());
        boolean update = lambdaUpdate().set(MintHangSale::getWithdrawFreeHash, hash).set(MintHangSale::getStatus, 3).set(MintHangSale::getUpdateTime, LocalDateTime.now()).eq(MintHangSale::getId, dataId).update();
        if (!update) {
        }
        MintHangSale mintHangSale = getById(dataId);
        String mintJson = "data:,{\"p\":\"denim-20\",\"op\":\"transfer\",\"tick\":\"btia\",\"amt\":\"" + mintHangSale.getMintNum().setScale(0, RoundingMode.DOWN).toPlainString() + "\"}" + mintHangSale.getId();
        String privateKey = "aaa";
        sendJson(privateKey, userAddress, Hex.stringToHex(mintJson));
        nonceMarketService.updateNonce(userAddress);
        if (redisUtil.getString(RedisConstant.USER_MARKET_TRANSFER_LOCK + userAddress) != null) {
            redisUtil.delete(RedisConstant.USER_MARKET_TRANSFER_LOCK + userAddress );
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
}
