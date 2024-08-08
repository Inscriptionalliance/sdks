package com.nft.cn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.RedisConstant;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.BtiaPledgeMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.Hex;
import com.nft.cn.util.RedisUtil;
import com.nft.cn.util.UUID;
import com.nft.cn.vo.req.MintTransferAuthReq;
import com.nft.cn.vo.resp.BtiaPledgeResp;
import com.nft.cn.vo.resp.PledgeInfoResp;
import com.nft.cn.vo.resp.PledgeStatusResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BtiaPledgeServiceImpl extends ServiceImpl<BtiaPledgeMapper, BtiaPledge> implements BtiaPledgeService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintUserHoldService mintUserHoldService;
    @Autowired
    private MintDeployService mintDeployService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UUserAuthNumService uUserAuthNumService;

    @Override
    public BaseResult<PledgeStatusResp> pledgeStatus() {
        UUser tokenUser = uUserService.getTokenUser();
        Integer count = lambdaQuery().eq(BtiaPledge::getUserAddress, tokenUser.getUserAddress()).eq(BtiaPledge::getStatus, 1).count();
        PledgeStatusResp resp = new PledgeStatusResp();
        if (count != null && count > 0) {
            resp.setIsPledge(1);
        } else {
            resp.setIsPledge(0);
        }
        return BaseResult.success(resp);
    }

    @Override
    public BaseResult<BtiaPledgeResp> pledge() {
        UUser tokenUser = uUserService.getTokenUser();
        String mintBtiaPledgeAddress = sSystemConfigService.getByKey(SystemConfigConstant.mint_btia_pledge_address).getConfigValue();
        String mintBtiaPledgeNum = sSystemConfigService.getByKey(SystemConfigConstant.mint_btia_pledge_num).getConfigValue();
        if (StringUtils.isEmpty(mintBtiaPledgeNum)) {
            return BaseResult.fail(i18nService.getMessage("88888"));
        }
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(tokenUser.getUserAddress());
        if (uUserBaseInfo.getIsVip() != 1 && uUserBaseInfo.getIsNodeVip() != 1 && uUserBaseInfo.getIsAdvancedVip() != 1 && uUserBaseInfo.getIsStandardVip() != 1) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        if (mintDeploy == null) {
            return BaseResult.fail(i18nService.getMessage("20081"));
        }
        MintUserHold mintUserHold = mintUserHoldService.getMintUserHold(tokenUser.getUserAddress(), mintDeploy.getId());
        if (mintUserHold.getMintTransferStatus() != 1) {
            return BaseResult.fail(i18nService.getMessage("20093"));
        }
        if (mintUserHold.getMintNum().compareTo(new BigDecimal(mintBtiaPledgeNum)) < 0) {
            return BaseResult.fail(i18nService.getMessage("20085"));
        }
        String mintJson = "data:,{\"p\":\"denim-20\",\"op\":\"transfer\",\"tick\":\"btia\",\"amt\":\"" + mintBtiaPledgeNum + "\"}";
        String mintJsonHex = Hex.stringToHex(mintJson);
        BtiaPledgeResp resp = new BtiaPledgeResp();
        resp.setMintJsonHex(mintJsonHex);
        resp.setToAddress(mintBtiaPledgeAddress);
        UUserAuthNum uUserAuthNum = uUserAuthNumService.refreshAuthNum(tokenUser, 3);
        resp.setAuthNum(uUserAuthNum.getMintTransferAuthNum());
        return BaseResult.success(resp);
    }

    @Override
    public BaseResult<PledgeInfoResp> pledgeInfo() {
        UUser tokenUser = uUserService.getTokenUser();
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(tokenUser.getUserAddress());
        PledgeInfoResp resp = new PledgeInfoResp();
        if (uUserBaseInfo.getIsAdvancedVip() == 1) {
            resp.setIdentity(3);
        } else if (uUserBaseInfo.getIsVip() == 1) {
            resp.setIdentity(1);
        } else if (uUserBaseInfo.getIsNodeVip() == 1) {
            resp.setIdentity(2);
        } else if (uUserBaseInfo.getIsStandardVip() == 1) {
            resp.setIdentity(4);
        } else {
            resp.setIdentity(0);
        }
        String mintBtiaPledgeNum = sSystemConfigService.getByKey(SystemConfigConstant.mint_btia_pledge_num).getConfigValue();
        if (!StringUtils.isEmpty(mintBtiaPledgeNum)) {
            resp.setPledgeAmount(new BigDecimal(mintBtiaPledgeNum));
        }
        return BaseResult.success(resp);
    }
}
