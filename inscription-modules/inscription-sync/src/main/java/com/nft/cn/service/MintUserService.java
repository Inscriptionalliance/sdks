package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintUser;
import com.nft.cn.vo.DataSyncVO;

import java.math.BigDecimal;

public interface MintUserService extends IService<MintUser> {

    void userMintSync(DataSyncVO dataSyncVO, String hash, Long blockNum, String mintContractAddress);

    void userMintSyncTest(DataSyncVO dataSyncVO, String hash, Long blockNum, String mintContractAddress);

    void userMintSynctest(DataSyncVO dataSyncVO, String hash, Long blockNum, String mintContractAddress);

    void mintStatusScheduled();

    BigDecimal sumMintNum(Long userId, Integer status);

    BigDecimal sumTeamMintNum(Long userId, Integer status);

}
