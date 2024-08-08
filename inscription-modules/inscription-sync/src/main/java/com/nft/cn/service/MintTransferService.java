package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintTransfer;

public interface MintTransferService extends IService<MintTransfer> {

    void mint(String from, String to, String hash, String mintStr, Long blockNum);

    void hangSale(String from, String to, String hash, String mintJson, Long blockNum);

    void withdraw(String from, String to, String hash, String mintJson, Long blockNum);

    void pay(String from, String to, String hash, String mintJson, Long blockNum);

    void mintBridgeCoin(String from, String to, String hash, String mintJson, Long blockNum);

    void coinSwapMint(String from, String to, String hash, String mintJson, Long blockNum);

    void transfer(String from, String to, String hash, String mintJson, Long blockNum);

    void btiaPledge(String from, String to, String hash, String mintJson, Long blockNum);

    void btiaReceive(String from, String to, String hash, String mintJson, Long blockNum);
}
