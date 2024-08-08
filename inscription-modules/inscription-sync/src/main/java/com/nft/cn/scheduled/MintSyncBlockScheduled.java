package com.nft.cn.scheduled;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nft.cn.config.AsyncConfig;
import com.nft.cn.config.RabbitMqConfig;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.Hex;
import com.nft.cn.util.HtRPCApiUtils;
import com.nft.cn.util.RedisUtil;
import com.nft.cn.vo.MintSyncVO;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Reference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


@Component
@Slf4j
public class MintSyncBlockScheduled {

    @Value("${rpcUrl}")
    private String rpcUrl;

    @Value("${quicknodeUrl}")
    private String quicknodeUrl;

    @Autowired
    private SSystemConfigService systemConfigService;

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Autowired
    private UUserService uUserService;

    @Autowired
    private MintTransferService mintTransferService;
    @Autowired
    private MintHangSaleService mintHangSaleService;
//    @Autowired
//    private RedisUtil redisUtil;
    @Autowired
    private AsyncConfig asyncConfig;


//    @Transactional(rollbackFor = Exception.class)
//    @Scheduled(fixedDelay = 3000)
    public void mintSyncBlockScheduled() throws Throwable {
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        SSystemConfig sSystemConfig = systemConfigService.getByKey(SystemConfigConstant.sync_bsc_mint_block_number);
        if (!Optional.ofNullable(sSystemConfig).isPresent()) {
            return;
        }
        BigInteger systemBlockNum = BigInteger.valueOf(Long.parseLong(sSystemConfig.getConfigValue()));

        BigInteger lastBlockNumber = null;
        try {
            lastBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();
        } catch (Exception e) {
            return;
        }
        if (lastBlockNumber.subtract(systemBlockNum).intValue() < 2) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        while (true) {
            SSystemConfig sSystemConfig1 = systemConfigService.getByKey(SystemConfigConstant.sync_bsc_mint_block_number);
            if (!Optional.ofNullable(sSystemConfig1).isPresent()) {
                return;
            }

            BigInteger systemBlockNum1 = BigInteger.valueOf(Long.parseLong(sSystemConfig1.getConfigValue()));

            if (lastBlockNumber.subtract(systemBlockNum1).intValue() < 2) {
                return;
            }


            String jsonStr = "{\"method\":\"trace_replayBlockTransactions\",\"params\":[\"0x" + Hex.toHexNumber(systemBlockNum1) + "\",[\"trace\"]],\"id\":1,\"jsonrpc\":\"2.0\"}";
            HttpResponse response = HttpRequest.post(quicknodeUrl).header("Content-Type", "application/json").body(jsonStr).execute();
            if (response.getStatus() != 200) {
                return;
            }
            checkMintData(response.body(), systemBlockNum1.longValue());
            sSystemConfig1.setConfigValue(systemBlockNum1.add(new BigInteger("1")).toString());

            boolean flag = systemConfigService.lambdaUpdate()
                    .set(SSystemConfig::getConfigValue, systemBlockNum1.add(new BigInteger("1")).toString())
                    .eq(SSystemConfig::getConfigKey, SystemConfigConstant.sync_bsc_mint_block_number)
                    .update();
            if (!flag) {
            }
        }
    }

    public void checkMintData(String body, Long blockNum) {
        if (StringUtils.isEmpty(body)) {
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(body);
        if (jsonObject == null) {
            return;
        }
        JSONArray resultArr = jsonObject.getJSONArray("result");
        if (resultArr == null) {
            return;
        }
        String mintListingAddress = systemConfigService.getByKey(SystemConfigConstant.mint_listing_address).getConfigValue();
        String mintSwapAddress = systemConfigService.getByKey(SystemConfigConstant.mint_swap_address).getConfigValue();
        String mintBtiaPledgeAddress = systemConfigService.getByKey(SystemConfigConstant.mint_btia_pledge_address).getConfigValue();

        List<Callable<MintSyncVO>> callableListAll = new ArrayList<>();
        for (int i = 0; i < resultArr.size(); i++) {
            List<Callable<MintSyncVO>> callableList = callableStack(resultArr.getJSONObject(i), blockNum, mintListingAddress, mintSwapAddress, mintBtiaPledgeAddress);
            callableListAll.addAll(callableList);
        }
        List<MintSyncVO> mintSyncVOList = new ArrayList<>();
        ThreadPoolExecutor asyncExecutor = asyncConfig.asyncExecutorMint();
        try {
            List<Future<MintSyncVO>> futures = asyncExecutor.invokeAll(callableListAll);
            for (Future<MintSyncVO> future : futures) {
                MintSyncVO mintSyncVO = future.get();
                if (mintSyncVO != null) {
                    mintSyncVOList.add(mintSyncVO);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
        }
        if (!CollectionUtils.isEmpty(mintSyncVOList)) {
            List<MintSyncVO> mintRankCommunityListOrder = mintSyncVOList.stream().sorted(Comparator.comparing(MintSyncVO::getBlockNum)).collect(Collectors.toList());
            for (MintSyncVO mintSyncVO : mintRankCommunityListOrder) {
                try {
                    checkMintData(mintSyncVO.getFrom(), mintSyncVO.getTo(), mintSyncVO.getHash(), mintSyncVO.getMintJson(), mintSyncVO.getBlockNum());
                } catch (Exception e) {
                }
            }
        }

    }

    public List<Callable<MintSyncVO>> callableStack(JSONObject result, Long blockNum, String mintListingAddress, String mintSwapAddress, String mintBtiaPledgeAddress){
        if (result == null) {
            return null;
        }
        String hash = result.getString("transactionHash");
        if (StringUtils.isEmpty(hash)) {
            return null;
        }
        JSONArray traceArr = result.getJSONArray("trace");
        if (traceArr == null) {
            return null;
        }
        List<Callable<MintSyncVO>> callableList = new ArrayList<>();
        for (int i1 = 0; i1 < traceArr.size(); i1++) {
            int finalI = i1;
            Callable<MintSyncVO> callable = new Callable<MintSyncVO>() {
                @Override
                public MintSyncVO call() throws Exception {
                    return callableCStatck(traceArr.getJSONObject(finalI), hash, blockNum, mintListingAddress, mintSwapAddress, mintBtiaPledgeAddress);
                }
            };
            callableList.add(callable);
        }
        return callableList;
    }

    public MintSyncVO callableCStatck(JSONObject trace, String hash, Long blockNum, String mintListingAddress, String mintSwapAddress, String mintBtiaPledgeAddress){
        if (trace == null) {
            return null;
        }
        JSONObject action = trace.getJSONObject("action");
        if (action == null) {
            return null;
        }
        String callType = action.getString("callType");
        if (StringUtils.isEmpty(callType) || !callType.equals("call")) {
            return null;
        }
        String from = action.getString("from");
        if (StringUtils.isEmpty(from)) {
            return null;
        }
        UUser fromUser = uUserService.getByUserAddress(from);
        if (fromUser == null && !from.equalsIgnoreCase(mintListingAddress) && !from.equalsIgnoreCase(mintSwapAddress) && !from.equalsIgnoreCase(mintBtiaPledgeAddress)) {
            return null;
        }
        String to = action.getString("to");
        if (StringUtils.isEmpty(to)) {
            return null;
        }
        UUser toUser = uUserService.getByUserAddress(to);
        if (toUser == null && !to.equalsIgnoreCase(mintListingAddress) && !to.equalsIgnoreCase(mintSwapAddress) && !to.equalsIgnoreCase(mintBtiaPledgeAddress)) {
            return null;
        }
        String input = action.getString("input");
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        MintSyncVO mintSyncVO = new MintSyncVO();
        mintSyncVO.setFrom(from);
        mintSyncVO.setTo(to);
        mintSyncVO.setHash(hash);
        mintSyncVO.setMintJson(input);
        mintSyncVO.setBlockNum(blockNum);
        try {
            return mintSyncVO;
        } catch (Exception e) {
            return null;
        }
    }


    public void checkMintData(String from, String to, String hash, String mintStr, Long blockNum) throws Exception{
        Integer count = mintTransferService.lambdaQuery().eq(MintTransfer::getHash, hash).count();
        if (count != null && count > 0) {
            return;
        }
        String mintListingAddress = systemConfigService.getByKey(SystemConfigConstant.mint_listing_address).getConfigValue();
        String mintSwapAddress = systemConfigService.getByKey(SystemConfigConstant.mint_swap_address).getConfigValue();
        String mintBtiaPledgeAddress = systemConfigService.getByKey(SystemConfigConstant.mint_btia_pledge_address).getConfigValue();
        if (from.equalsIgnoreCase(to)) {
            mintTransferService.mint(from, to, hash, mintStr, blockNum);
        } else if (to.equalsIgnoreCase(mintListingAddress)) {
            String mintJson = Hex.hexToString(mintStr.substring(2));
            mintTransferService.hangSale(from, to, hash, mintJson, blockNum);
        } else if (from.equalsIgnoreCase(mintListingAddress)) {
            String mintJson = Hex.hexToString(mintStr.substring(2));
            BigDecimal id = new BigDecimal(mintJson.substring(mintJson.lastIndexOf("}") + 1));
            MintHangSale mintHangSale = mintHangSaleService.lambdaQuery().eq(MintHangSale::getUserAddress, from).eq(MintHangSale::getId, id.longValue()).one();
            if (mintHangSale == null) {
            } else if (mintHangSale.getStatus() == 3) {
                mintTransferService.withdraw(from, to, hash, mintJson, blockNum);
            } else if (mintHangSale.getStatus() == 4 && mintHangSale.getPayUserAddress().equalsIgnoreCase(to)) {
                mintTransferService.pay(from, to, hash, mintJson, blockNum);
            } else {
            }
        } else if (to.equalsIgnoreCase(mintSwapAddress)) {
            String mintJson = Hex.hexToString(mintStr.substring(2));
            mintTransferService.mintBridgeCoin(from, to, hash, mintJson, blockNum);
        } else if (from.equalsIgnoreCase(mintSwapAddress)) {
            String mintJson = Hex.hexToString(mintStr.substring(2));
            mintTransferService.coinSwapMint(from, to, hash, mintJson, blockNum);
        } else if (to.equalsIgnoreCase(mintBtiaPledgeAddress)) {
            String mintJson = Hex.hexToString(mintStr.substring(2));
            mintTransferService.btiaPledge(from, to, hash, mintJson, blockNum);
        } else if (from.equalsIgnoreCase(mintBtiaPledgeAddress)) {
            String mintJson = Hex.hexToString(mintStr.substring(2));
            mintTransferService.btiaReceive(from, to, hash, mintJson, blockNum);
        } else {
            String mintJson = Hex.hexToString(mintStr.substring(2));
            mintTransferService.transfer(from, to, hash, mintJson, blockNum);
        }


    }




}


