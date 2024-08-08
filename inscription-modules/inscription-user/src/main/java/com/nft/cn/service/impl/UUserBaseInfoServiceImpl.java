package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.UUserBaseInfoMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.resp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class UUserBaseInfoServiceImpl extends ServiceImpl<UUserBaseInfoMapper, UUserBaseInfo> implements UUserBaseInfoService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private UUserRefereeNumService uUserRefereeNumService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private UUserTwitterTokenService uUserTwitterTokenService;
    @Autowired
    private TwitterService twitterService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private CreditRecordService creditRecordService;
    @Autowired
    private FreeTicketRecordService freeTicketRecordService;
    @Autowired
    private MintTicketRecordService mintTicketRecordService;
    @Autowired
    private EpicTicketRecordService epicTicketRecordService;
    @Autowired
    private RarityTicketRecordService rarityTicketRecordService;
    @Autowired
    private MintRankService mintRankService;
    @Autowired
    private MintDeployService mintDeployService;
    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private UUserRefereeService uUserRefereeService;
    @Autowired
    private ZUserIpService zUserIpService;

    @Override
    public UUserBaseInfo getUUserBaseInfo(String userAddress) {
        UUserBaseInfo uUserBaseInfo = lambdaQuery().eq(UUserBaseInfo::getUserAddress, userAddress).one();
        if (uUserBaseInfo == null) {
            UUser uUser = uUserService.getByUserAddress(userAddress);
            if (uUser != null) {
                uUserBaseInfo = new UUserBaseInfo();
                uUserBaseInfo.setUserId(uUser.getId());
                uUserBaseInfo.setUserAddress(uUser.getUserAddress());
                uUserBaseInfo.setUpdateTime(LocalDateTime.now());
                uUserBaseInfo.setCreateTime(LocalDateTime.now());
                save(uUserBaseInfo);
            }
        } else {
            return uUserBaseInfo;
        }
        return lambdaQuery().eq(UUserBaseInfo::getUserAddress, userAddress).one();
    }

    @Override
    public UUserBaseInfo getUUserBaseInfo(Long userId) {
        UUserBaseInfo uUserBaseInfo = lambdaQuery().eq(UUserBaseInfo::getUserId, userId).one();
        if (uUserBaseInfo == null) {
            UUser uUser = uUserService.getById(userId);
            if (uUser != null) {
                uUserBaseInfo = new UUserBaseInfo();
                uUserBaseInfo.setUserId(uUser.getId());
                uUserBaseInfo.setUserAddress(uUser.getUserAddress());
                uUserBaseInfo.setUpdateTime(LocalDateTime.now());
                uUserBaseInfo.setCreateTime(LocalDateTime.now());
                save(uUserBaseInfo);
            }
        } else {
            return uUserBaseInfo;
        }
        return lambdaQuery().eq(UUserBaseInfo::getUserId, userId).one();
    }

    @Override
    public TeamInfoResp teamInfo() {
        UUser tokenUser = uUserService.getTokenUser();
        UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(tokenUser.getUserAddress());
        TeamInfoResp resp = new TeamInfoResp();
        resp.setIsVip(uUserBaseInfo.getIsVip());
        resp.setIsNodeVip(uUserBaseInfo.getIsNodeVip());
        resp.setIsAdvancedVip(uUserBaseInfo.getIsAdvancedVip());
        resp.setIsStandardVip(uUserBaseInfo.getIsStandardVip());
        resp.setIsWhite(uUserBaseInfo.getIsWhite());
        resp.setFreeTicket(uUserBaseInfo.getFreeTicket());
        resp.setMintTicket(uUserBaseInfo.getMintTicket());
        resp.setCredit(uUserBaseInfo.getCredit());
        resp.setUserAddress(tokenUser.getUserAddress());
        MintRank mintRank = mintRankService.lambdaQuery().eq(MintRank::getUserAddress, tokenUser.getUserAddress()).one();
        if (mintRank != null) {
            resp.setRank(mintRank.getRank());
        }
        Integer refereeNum = uUserRefereeService.lambdaQuery().eq(UUserReferee::getRefereeUserId, tokenUser.getId()).count();
        resp.setRefereeNum(refereeNum.longValue());
        Integer validRefereeNum = uUserRefereeNumService.lambdaQuery().eq(UUserRefereeNum::getUserAddress, tokenUser.getUserAddress()).eq(UUserRefereeNum::getStatus, 0).count();
        resp.setValidRefereeNum(validRefereeNum.longValue());
        UUserTwitterToken uUserTwitterToken = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserAddress, tokenUser.getUserAddress()).one();
        if (uUserTwitterToken != null) {
            resp.setTwitterUsername(uUserTwitterToken.getTwitterUsername());
        }
        return resp;
    }

    @Override
    public BottomDataResp bottomData() {
        BottomDataResp resp = new BottomDataResp();
        int count = uUserService.count();
        resp.setUserNum((long) count);
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        resp.setTotalCirculation(mintDeploy.getMintAll().longValue());
        MintUser mintUser = mintUserService.getOne(new QueryWrapper<MintUser>().select("sum(mint_num) as mintNum").eq("status", 4));
        if (mintUser != null) {
            BigDecimal castRatio = mintUser.getMintNum().multiply(new BigDecimal("100")).divide(mintDeploy.getMintAll().multiply(mintDeploy.getMintOne()), 2, RoundingMode.DOWN);
            resp.setMintRatio(castRatio);
        } else {
            resp.setMintRatio(BigDecimal.ZERO);
        }
        return resp;
    }


    @Override
    public BaseResult<String> checkBindTwitter() {
        UUser tokenUser = uUserService.getTokenUser();
        UUserTwitterToken uUserTwitterToken = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserAddress, tokenUser.getUserAddress()).one();
        if (uUserTwitterToken != null && !StringUtils.isEmpty(uUserTwitterToken.getTwitterId())) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(tokenUser.getUserAddress());
            Integer currentPage = uUserBaseInfo.getCurrentPage();
            if (currentPage < 4) {
                currentPage = 4;
            }
            lambdaUpdate().set(UUserBaseInfo::getCurrentPage, currentPage).eq(UUserBaseInfo::getUserAddress, tokenUser.getUserAddress()).update();
            return BaseResult.success();
        } else {
            return BaseResult.fail(i18nService.getMessage("20034"));
        }
    }

    @Override
    public BaseResult<String> checkFollowsTwitter() {
        UUser tokenUser = uUserService.getTokenUser();
        UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(tokenUser.getUserAddress());
        if (uUserBaseInfo.getIsInterestTwitter() == 0) {
            UUserTwitterToken uUserTwitterToken = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserAddress, tokenUser.getUserAddress()).one();
            if (uUserTwitterToken == null) {
                return BaseResult.fail(i18nService.getMessage("20034"));
            }
            String officialTwitterId = sSystemConfigService.getByKey(SystemConfigConstant.official_twitter_id).getConfigValue();
            BaseResult<String> stringBaseResult = twitterService.checkFollows(officialTwitterId, uUserTwitterToken);
            if (stringBaseResult.getCode() == 200) {
                String followsTwitterIncomeUnit = sSystemConfigService.getByKey(SystemConfigConstant.follows_twitter_income_unit).getConfigValue();
                String[] followsTwitterIncomeUnitArr = followsTwitterIncomeUnit.split(",");
                String followsTwitterIncomeNum = sSystemConfigService.getByKey(SystemConfigConstant.follows_twitter_income_num).getConfigValue();
                String[] followsTwitterIncomeNumArr = followsTwitterIncomeNum.split(",");
                for (int i = 0; i < followsTwitterIncomeUnitArr.length; i++) {
                    int incomeUnit = Integer.parseInt(followsTwitterIncomeUnitArr[i]);
                    BigDecimal incomeNum = new BigDecimal(followsTwitterIncomeNumArr[i]);
                    if (incomeUnit == 1) {
                        uUserBaseInfoService.updateCredit(tokenUser.getUserAddress(), incomeNum, 103, null);
                    } else if (incomeUnit == 2) {
                        uUserBaseInfoService.updateMintTicket(tokenUser.getUserAddress(), incomeNum, 103, null);
                    } else if (incomeUnit == 3) {
                        uUserBaseInfoService.updateFreeTicket(tokenUser.getUserAddress(), incomeNum, 103, null);
                    }
                }
                lambdaUpdate().set(UUserBaseInfo::getIsInterestTwitter, 1).eq(UUserBaseInfo::getUserAddress, tokenUser.getUserAddress()).update();
            } else {
                return BaseResult.fail(i18nService.getMessage("20022"));
            }
        }
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> joinDiscord() {
        UUser tokenUser = uUserService.getTokenUser();
        UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(tokenUser.getUserAddress());
        Integer currentPage = uUserBaseInfo.getCurrentPage();
        if (currentPage < 5) {
            currentPage = 5;
        }
        lambdaUpdate().set(UUserBaseInfo::getIsJoinDiscord, 1).set(UUserBaseInfo::getCurrentPage, currentPage).eq(UUserBaseInfo::getUserAddress, tokenUser.getUserAddress()).update();
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> checkRequirement() {
        UUser tokenUser = uUserService.getTokenUser();
        UUserReferee uUserReferee = uUserRefereeService.selectByUserAddress(tokenUser.getUserAddress());
        if (uUserReferee.getIncomeStatus() == 1) {
            return BaseResult.success();
        }
        UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(tokenUser.getUserAddress());
        if (uUserBaseInfo.getIsBindOkx() == 0) {
            return BaseResult.fail(i18nService.getMessage("20020"));
        }
        if (uUserBaseInfo.getIsBindUnisat() == 0) {
            return BaseResult.fail(i18nService.getMessage("20020"));
        }
        if (uUserBaseInfo.getIsBindReferee() == 0) {
            return BaseResult.fail(i18nService.getMessage("20021"));
        }
        Integer currentPage = uUserBaseInfo.getCurrentPage();
        if (currentPage < 6) {
            currentPage = 6;
        }
        lambdaUpdate().set(UUserBaseInfo::getCurrentPage, currentPage).eq(UUserBaseInfo::getUserAddress, tokenUser.getUserAddress()).update();
        awardLoginIncome(tokenUser.getUserAddress());
        awardRefereeIncome(tokenUser.getUserAddress());
        uUserRefereeNumService.awardRefereeNum(tokenUser);
        return BaseResult.success();
    }

    @Override
    public void awardLoginIncome(String userAddress) {
        BigDecimal loginIncomeCredit = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.login_income_credit).getConfigValue());
        BigDecimal loginIncomeMint = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.login_income_mint).getConfigValue());
        updateMintTicket(userAddress, loginIncomeMint, 101, null);
        updateCredit(userAddress, loginIncomeCredit, 101, null);
    }

    @Override
    public void awardRefereeIncome(String userAddress) {
        UUserReferee uUserReferee = uUserRefereeService.selectByUserAddress(userAddress);
        UUser byId = uUserService.getById(uUserReferee.getRefereeUserId());
        if (byId == null) {
            return;
        }
        BigDecimal refereeIncomeCredit = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.referee_income_credit).getConfigValue());
        updateCredit(byId.getUserAddress(), refereeIncomeCredit, 102, null);
        BigDecimal refereeIncomeMintRatio = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.referee_income_mint_ratio).getConfigValue());
        BigDecimal loginIncomeMint = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.login_income_mint).getConfigValue());
        updateMintTicket(byId.getUserAddress(), refereeIncomeMintRatio.multiply(loginIncomeMint), 102, null);
        uUserRefereeNumService.lambdaUpdate()
                .set(UUserRefereeNum::getIncomeStatus, 1)
                .set(UUserRefereeNum::getUpdateTime, LocalDateTime.now())
                .eq(UUserRefereeNum::getUserId, byId.getId())
                .update();
        uUserRefereeService.lambdaUpdate()
                .set(UUserReferee::getIncomeStatus, 1)
                .eq(UUserReferee::getUserAddress, userAddress)
                .update();
        Integer count = uUserRefereeService.lambdaQuery().eq(UUserReferee::getRefereeUserId, byId.getId()).eq(UUserReferee::getIncomeStatus, 1).count();
        Integer refereeIncomeNum = Integer.parseInt(sSystemConfigService.getByKey(SystemConfigConstant.referee_income_num).getConfigValue());
        if (count.equals(refereeIncomeNum)) {
            BigDecimal refereeIncomeNumIncome = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.referee_income_num_income).getConfigValue());
            updateMintTicket(byId.getUserAddress(), refereeIncomeNumIncome, 108, null);
        }
    }

    @Override
    public BaseResult<String> joinSystem() {
        UUser tokenUser = uUserService.getTokenUser();
        UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(tokenUser.getUserAddress());
        Integer currentPage = uUserBaseInfo.getCurrentPage();
        if (currentPage < 7) {
            currentPage = 7;
        }
        lambdaUpdate().set(UUserBaseInfo::getCurrentPage, currentPage).eq(UUserBaseInfo::getUserAddress, tokenUser.getUserAddress()).update();
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> interestTwitter() {
        UUser tokenUser = uUserService.getTokenUser();
        UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(tokenUser.getUserAddress());
        Integer currentPage = uUserBaseInfo.getCurrentPage();
        if (currentPage < 4) {
            if (uUserBaseInfo.getIsForwardTwitter() == 1) {
                currentPage = 4;
            } else {
                currentPage = 3;
            }
        }
        lambdaUpdate().set(UUserBaseInfo::getIsInterestTwitter, 1).set(UUserBaseInfo::getCurrentPage, currentPage).eq(UUserBaseInfo::getUserAddress, tokenUser.getUserAddress()).update();
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> forwardTwitter() {
        UUser tokenUser = uUserService.getTokenUser();
        UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(tokenUser.getUserAddress());
        Integer currentPage = uUserBaseInfo.getCurrentPage();
        if (currentPage < 4) {
            if (uUserBaseInfo.getIsInterestTwitter() == 1) {
                currentPage = 4;
            } else {
                currentPage = 3;
            }
        }
        lambdaUpdate().set(UUserBaseInfo::getIsInterestTwitter, 1).set(UUserBaseInfo::getCurrentPage, currentPage).eq(UUserBaseInfo::getUserAddress, tokenUser.getUserAddress()).update();
        return BaseResult.success();
    }

    @Override
    public BaseResult<InterestTwitterInfoResp> interestTwitterInfo() {
        String lang = tokenService.getLang();
        InterestTwitterInfoResp resp = new InterestTwitterInfoResp();
        if (Locale.CHINESE.getLanguage().equalsIgnoreCase(lang)) {
            resp.setDepict("awd");
        } else {
            resp.setDepict("officialX");
        }
        resp.setTwitterUsername("InscripAlliance");
        return BaseResult.success(resp);
    }

    @Override
    public boolean updateFreeTicket(String userAddress, BigDecimal freeTicketNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            BigDecimal freeTicket = uUserBaseInfo.getFreeTicket();
            BigDecimal freeTicketNew = BigDecimal.ZERO;
            if (type < 200) {
                freeTicketNew = freeTicket.add(freeTicketNum);
            } else if (type > 200) {
                freeTicketNew = freeTicket.subtract(freeTicketNum);
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (freeTicketNew.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getFreeTicket, freeTicketNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getFreeTicket, freeTicket)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        freeTicketRecordService.saveFreeTicketRecord(userAddress, freeTicketNum, type, relationAddress);
        return true;
    }


    @Override
    public boolean updateMintTicket(String userAddress, BigDecimal mintTicketNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            BigDecimal mintTicket = uUserBaseInfo.getMintTicket();
            BigDecimal mintTicketNew = BigDecimal.ZERO;
            if (type < 200) {
                mintTicketNew = mintTicket.add(mintTicketNum);
            } else if (type > 200) {
                mintTicketNew = mintTicket.subtract(mintTicketNum);
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (mintTicketNew.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getMintTicket, mintTicketNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getMintTicket, mintTicket)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mintTicketRecordService.saveMintTicketRecord(userAddress, mintTicketNum, type, relationAddress);
        return true;
    }

    @Override
    public boolean updateCredit(String userAddress, BigDecimal creditNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            BigDecimal credit = uUserBaseInfo.getCredit();
            BigDecimal creditNew = BigDecimal.ZERO;
            if (type < 200) {
                creditNew = credit.add(creditNum);
            } else if (type > 200) {
                creditNew = credit.subtract(creditNum);
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (creditNew.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getCredit, creditNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getCredit, credit)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        creditRecordService.saveCreditRecord(userAddress, creditNum, type, relationAddress);
        return true;
    }

    @Override
    public boolean updateRarityTicket(String userAddress, Long rarityTicketNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            Long rarityTicket = uUserBaseInfo.getRarityTicket();
            Long rarityTicketNew = 0L;
            if (type < 200) {
                rarityTicketNew = rarityTicket + rarityTicketNum;
            } else if (type > 200) {
                rarityTicketNew = rarityTicket - rarityTicketNum;
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (rarityTicketNew < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getRarityTicket, rarityTicketNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getRarityTicket, rarityTicket)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        rarityTicketRecordService.saveRarityTicket(userAddress, rarityTicketNum, type, relationAddress);
        return true;
    }

    @Override
    public boolean updateEpicTicket(String userAddress, Long epicTicketNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            Long epicTicket = uUserBaseInfo.getEpicTicket();
            Long epicTicketNew = 0L;
            if (type < 200) {
                epicTicketNew = epicTicket + epicTicketNum;
            } else if (type > 200) {
                epicTicketNew = epicTicket - epicTicketNum;
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (epicTicketNew < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getEpicTicket, epicTicketNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getEpicTicket, epicTicket)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        epicTicketRecordService.saveEpicTicket(userAddress, epicTicketNum, type, relationAddress);
        return true;
    }

    @Override
    public LoginIncomeResp loginIncome() {
        BigDecimal loginIncomeCredit = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.login_income_credit).getConfigValue());
        BigDecimal loginIncomeMint = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.login_income_mint).getConfigValue());
        LoginIncomeResp resp = new LoginIncomeResp();
        resp.setLoginIncomeCredit(loginIncomeCredit);
        resp.setLoginIncomeMint(loginIncomeMint);
        return resp;
    }

    @Override
    public List<UUserBaseInfo> getVipUserList(Long userId) {
        return baseMapper.getVipUserList(userId);
    }
}
