package com.nft.cn.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nft.cn.config.RabbitMqConfig;
import com.nft.cn.entity.BridgeRecordBinanceBrc20Btia;
import com.nft.cn.entity.MintHangSale;
import com.nft.cn.entity.MintSwap;
import com.nft.cn.entity.MintUser;
import com.nft.cn.service.*;
import com.nft.cn.vo.DataSyncVO;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.web3j.protocol.core.methods.response.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class MQSyncBlockListener {

    @Value("${whiteContract}")
    private String whiteContract;
    @Value("${mintContractAddress}")
    private String mintContractAddress;
    @Value("${marketContractAddress}")
    private String marketContractAddress;
    @Value("${swapContractAddress}")
    private String swapContractAddress;
    @Value("${bridgeContractAddress}")
    private String bridgeContractAddress;

    @Autowired
    private WhitePayService whitePayService;
    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private MintHangSaleService mintHangSaleService;
    @Autowired
    private MintSwapService mintSwapService;
    @Autowired
    private BridgeRecordBinanceBrc20BtiaService bridgeRecordBinanceBrc20BtiaService;


//    @RabbitListener(queues = RabbitMqConfig.SCAN_QUEUE)
    public void scanBlock(String jsonResult, Message message, Channel channel) throws Throwable {
        if (!StringUtils.isEmpty(jsonResult)){
            Map map = JSONObject.parseObject(jsonResult, Map.class);
            if (!map.isEmpty()){

                List<Log> logList = JSONArray.parseArray(map.get("list").toString(), Log.class);
                String to = map.get("to").toString();
                String hash = map.get("hash").toString();
                Long blockNum = Long.parseLong(map.get("blockNum").toString());
                if (whiteContract.equalsIgnoreCase(to)){
                    AtomicReference<String> data = new AtomicReference<>();
                    AtomicInteger topicType = new AtomicInteger();
                    logList.forEach(logInfo -> {
                        if (logInfo.getTopics().get(0).equalsIgnoreCase("0xbac3d4ad3a08640666e4f14e0e21ebfe1c8c63488222caeae814473634b4cb1b")) {
                            data.set(logInfo.getData());
                            topicType.set(1);
                        }
                    });
                    if (data.get() == null) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        return;
                    }
                    if (topicType.get() == 1) {
                        whitePayService.syncPayWhite(data.get(), hash);
                    }
                } else if (mintContractAddress.equalsIgnoreCase(to)) {
                    Integer base = mintUserService.lambdaQuery().eq(MintUser::getPayHash, hash).count();
                    if (base != null && base > 0) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        return;
                    }
                    List<DataSyncVO> dataSyncVOList = new ArrayList<>();
                    logList.forEach(logInfo -> {
                        if (logInfo.getTopics().get(0).equalsIgnoreCase("0x92b26e707b024019810eb9f81fb26ab92c318a8efdc77204f029ed774a4ca84b")) {
                            DataSyncVO dataSyncVO = new DataSyncVO();
                            dataSyncVO.setData(logInfo.getData());
                            dataSyncVO.setTopicType(1);
                            dataSyncVOList.add(dataSyncVO);
                        }
                    });
                    if (CollectionUtils.isEmpty(dataSyncVOList)) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        return;
                    } else {
                        for (DataSyncVO dataSyncVO : dataSyncVOList) {
                            mintUserService.userMintSync(dataSyncVO, hash, blockNum, mintContractAddress);
                        }
                    }
                } else if (marketContractAddress.equalsIgnoreCase(to)) {
                    Integer base = mintHangSaleService.lambdaQuery().eq(MintHangSale::getWithdrawFreeHash, hash).or().eq(MintHangSale::getPayAmountHash, hash).count();
                    if (base != null && base > 0) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        return;
                    }
                    List<DataSyncVO> dataSyncVOList = new ArrayList<>();
                    logList.forEach(logInfo -> {
                        DataSyncVO dataSyncVO = new DataSyncVO();
                        dataSyncVO.setData(logInfo.getData());
                        if (logInfo.getTopics().get(0).equalsIgnoreCase("0x89f5adc174562e07c9c9b1cae7109bbecb21cf9d1b2847e550042b8653c54a0e")) {
                            dataSyncVO.setTopicType(1);
                            dataSyncVOList.add(dataSyncVO);
                        } else if (logInfo.getTopics().get(0).equalsIgnoreCase("0x0ac2bc6b60f17a6456dd65ed750ec0b5155602d39daa83b3038b5aafc0c24a29")) {
                            dataSyncVO.setTopicType(2);
                            dataSyncVOList.add(dataSyncVO);
                        }
                    });
                    if (CollectionUtils.isEmpty(dataSyncVOList)) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        return;
                    } else {
                        for (DataSyncVO dataSyncVO : dataSyncVOList) {
                            if (dataSyncVO.getTopicType() == 1) {
                                mintHangSaleService.pay(dataSyncVO, hash, blockNum, marketContractAddress);
                            } else if (dataSyncVO.getTopicType() == 2) {
                                mintHangSaleService.withdraw(dataSyncVO, hash, blockNum, marketContractAddress);
                            }

                        }
                    }
                } else if (swapContractAddress.equalsIgnoreCase(to)) {
                    Integer base = mintSwapService.lambdaQuery().eq(MintSwap::getContractHash, hash).count();
                    if (base != null && base > 0) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        return;
                    }
                    List<DataSyncVO> dataSyncVOList = new ArrayList<>();
                    logList.forEach(logInfo -> {
                        DataSyncVO dataSyncVO = new DataSyncVO();
                        dataSyncVO.setData(logInfo.getData());
                        if (logInfo.getTopics().get(0).equalsIgnoreCase("0x9734819749a91fc3be03ea83205f924ee08479bd3f0da48efc91d94d050cac1e")) {
                            dataSyncVO.setTopicType(1);
                            dataSyncVOList.add(dataSyncVO);
                        }
                    });
                    if (CollectionUtils.isEmpty(dataSyncVOList)) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        return;
                    } else {
                        for (DataSyncVO dataSyncVO : dataSyncVOList) {
                            if (dataSyncVO.getTopicType() == 1) {
                                mintSwapService.swap(dataSyncVO, hash, blockNum, swapContractAddress);
                            }

                        }
                    }
                } else if (bridgeContractAddress.equalsIgnoreCase(to)) {
                    Integer base = bridgeRecordBinanceBrc20BtiaService.lambdaQuery().eq(BridgeRecordBinanceBrc20Btia::getContractHash, hash).count();
                    if (base != null && base > 0) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        return;
                    }
                    List<DataSyncVO> dataSyncVOList = new ArrayList<>();
                    logList.forEach(logInfo -> {
                        DataSyncVO dataSyncVO = new DataSyncVO();
                        dataSyncVO.setData(logInfo.getData());
                        if (logInfo.getTopics().get(0).equalsIgnoreCase("0xa0d62d8363f007d4af76a2af5faf5afd32adb64f0e3cc760d2b06faa75c9fd7c")) {
                            dataSyncVO.setTopicType(1);
                            dataSyncVOList.add(dataSyncVO);
                        }
                    });
                    if (CollectionUtils.isEmpty(dataSyncVOList)) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        return;
                    } else {
                        for (DataSyncVO dataSyncVO : dataSyncVOList) {
                            if (dataSyncVO.getTopicType() == 1) {
                                bridgeRecordBinanceBrc20BtiaService.bridge(dataSyncVO, hash, blockNum, bridgeContractAddress);
                            }

                        }
                    }
                }
            }
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


}
