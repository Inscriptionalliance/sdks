package com.nft.cn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.MintTransferMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.Hex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class MintTransferServiceImpl extends ServiceImpl<MintTransferMapper, MintTransfer> implements MintTransferService {

    @Autowired
    private MintDeployService mintDeployService;
    @Autowired
    private MintUserHoldService mintUserHoldService;
    @Autowired
    private MintHangSaleService mintHangSaleService;
    @Autowired
    private MintSwapService mintSwapService;
    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private MintUserBoxService mintUserBoxService;
    @Autowired
    private BtiaPledgeService btiaPledgeService;
    @Autowired
    private BtiaIncomeService btiaIncomeService;
    @Autowired
    private BridgeRecordBinanceBrc20BtiaService bridgeRecordBinanceBrc20BtiaService;
    @Autowired
    private MintUserPaidService mintUserPaidService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private MintCommunityAddService mintCommunityAddService;

    @Override
    public void mint(String from, String to, String hash, String mintStr, Long blockNum) {

    }

    @Override
    public void hangSale(String from, String to, String hash, String mintJson, Long blockNum) {
    }

    @Override
    public void withdraw(String from, String to, String hash, String mintJson, Long blockNum) {

    }

    @Override
    public void pay(String from, String to, String hash, String mintJson, Long blockNum) {

    }

    @Override
    public void mintBridgeCoin(String from, String to, String hash, String mintJson, Long blockNum) {

    }

    @Override
    public void coinSwapMint(String from, String to, String hash, String mintJson, Long blockNum) {

    }

    @Override
    public void transfer(String from, String to, String hash, String mintJson, Long blockNum) {

    }

    @Override
    public void btiaPledge(String from, String to, String hash, String mintJson, Long blockNum) {

    }

    @Override
    public void btiaReceive(String from, String to, String hash, String mintJson, Long blockNum) {

    }

}
