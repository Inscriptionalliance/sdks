package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUserBaseInfo;

import java.math.BigDecimal;

public interface UUserBaseInfoService extends IService<UUserBaseInfo> {

    UUserBaseInfo getUUserBaseInfo(String userAddress);

    UUserBaseInfo getUUserBaseInfo(Long userId);

    boolean updateFreeTicket(String userAddress, BigDecimal freeTicketNum, Integer type, String relationAddress);

    boolean updateMintTicket(String userAddress, BigDecimal mintTicketNum, Integer type, String relationAddress);

    boolean updateCredit(String userAddress, BigDecimal creditNum, Integer type, String relationAddress);

    boolean updateRarityTicket(String userAddress, Long rarityTicketNum, Integer type, String relationAddress);

    boolean updateEpicTicket(String userAddress, Long epicTicketNum, Integer type, String relationAddress);
}
