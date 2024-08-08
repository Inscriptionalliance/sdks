package com.nft.cn.scheduled;

import com.alibaba.fastjson.JSONObject;
import com.nft.cn.config.RabbitMqConfig;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.entity.SSystemConfig;
import com.nft.cn.service.SSystemConfigService;
import com.nft.cn.util.HtRPCApiUtils;
import com.nft.cn.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.*;


@Component
@Slf4j
public class BscSyncBlockScheduled {

    @Value("${rpcUrl}")
    private String rpcUrl;

    @Value("${listenerContract}")
    private String listenerContract;

    @Autowired
    private SSystemConfigService systemConfigService;

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisUtil redisUtil;

//    @Transactional(rollbackFor = Exception.class)
//    @Scheduled(fixedDelay = 3000)
    public void bscSyncBlockScheduled() throws Throwable {
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        SSystemConfig sSystemConfig = systemConfigService.getByKey(SystemConfigConstant.sync_bsc_block_number);
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
            SSystemConfig sSystemConfig1 = systemConfigService.getByKey(SystemConfigConstant.sync_bsc_block_number);
            if (!Optional.ofNullable(sSystemConfig1).isPresent()) {
                return;
            }

            BigInteger systemBlockNum1 = BigInteger.valueOf(Long.parseLong(sSystemConfig1.getConfigValue()));

            if (lastBlockNumber.subtract(systemBlockNum1).intValue() < 2) {
                return;
            }
            List<String> contractList = Arrays.asList(listenerContract.toLowerCase().split(","));

            EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(new BigInteger(systemBlockNum1.toString())), true).send();

            List<EthBlock.TransactionResult> transactionResults = ethBlock.getResult().getTransactions();

            if (!transactionResults.isEmpty()) {
                for (EthBlock.TransactionResult<EthBlock.TransactionObject> transactionResult : transactionResults) {
                    EthBlock.TransactionObject transaction = transactionResult.get();

                    String to = transaction.getTo();
                    if (!contractList.contains(to)) {
                        continue;
                    }

                    String hash = transaction.getHash();
                    EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(hash).send();

                    Object statusObject = transactionReceipt.getTransactionReceipt().get().getStatus();
                    if (statusObject != null) {
                        String statusString = (String) statusObject;
                        BigInteger status = HtRPCApiUtils.toNum(statusString);
                        if (status.intValue() == 1) {

                            List<Log> list = transactionReceipt.getTransactionReceipt().get().getLogs();

                            if (list == null) {
                                continue;
                            }

                            Map<String, Object> map = new HashMap<>(5);
                            map.put("to", to);
                            map.put("hash", hash);
                            map.put("list", list);
                            map.put("blockNum", systemBlockNum1);
                            rabbitTemplate.convertAndSend(RabbitMqConfig.SCAN_EXCHANGE, RabbitMqConfig.SCAN_ROUTING_KEY, JSONObject.toJSONString(map));
                        }
                    }
                }
            }

            sSystemConfig1.setConfigValue(systemBlockNum1.add(new BigInteger("1")).toString());

            boolean flag = systemConfigService.lambdaUpdate()
                    .set(SSystemConfig::getConfigValue, systemBlockNum1.add(new BigInteger("1")).toString())
                    .eq(SSystemConfig::getConfigKey, SystemConfigConstant.sync_bsc_block_number)
                    .update();
            if (!flag) {
            }
        }
    }
}


