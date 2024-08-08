package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintUserPaid;

import java.math.BigDecimal;
import java.util.List;

public interface MintUserPaidService extends IService<MintUserPaid> {

    void savePaidMint(Long mintUserId);

    BigDecimal sumMintNum(Long userId, int phase);

    BigDecimal sumTeamMintNum(Long userId, int phase);

    BigDecimal sumRefereeMintNum(Long userId, int phase);

    List<MintUserPaid> syncAchieve(String statisticsValue);

}
