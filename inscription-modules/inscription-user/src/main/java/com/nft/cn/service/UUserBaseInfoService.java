package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUserBaseInfo;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.resp.*;

import java.math.BigDecimal;
import java.util.List;

public interface UUserBaseInfoService extends IService<UUserBaseInfo> {

    UUserBaseInfo getUUserBaseInfo(String userAddress);

    UUserBaseInfo getUUserBaseInfo(Long userId);

    TeamInfoResp teamInfo();

    BottomDataResp bottomData();

    BaseResult<String> checkBindTwitter();

    BaseResult<String> checkFollowsTwitter();

    BaseResult<String> joinDiscord();

    BaseResult<String> checkRequirement();

    void awardLoginIncome(String userAddress);

    void awardRefereeIncome(String userAddress);

    BaseResult<String> joinSystem();


    BaseResult<String> interestTwitter();

    BaseResult<String> forwardTwitter();

    BaseResult<InterestTwitterInfoResp> interestTwitterInfo();

    boolean updateFreeTicket(String userAddress, BigDecimal freeTicketNum, Integer type, String relationAddress);

    boolean updateMintTicket(String userAddress, BigDecimal mintTicketNum, Integer type, String relationAddress);

    boolean updateCredit(String userAddress, BigDecimal creditNum, Integer type, String relationAddress);

    boolean updateRarityTicket(String userAddress, Long rarityTicketNum, Integer type, String relationAddress);

    boolean updateEpicTicket(String userAddress, Long epicTicketNum, Integer type, String relationAddress);

    LoginIncomeResp loginIncome();

    List<UUserBaseInfo> getVipUserList(Long userId);

}
