package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.RedisConstant;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.MintTransferMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.Hex;
import com.nft.cn.util.RedisUtil;
import com.nft.cn.util.UUID;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MintTransferServiceImpl extends ServiceImpl<MintTransferMapper, MintTransfer> implements MintTransferService {

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
    private RedisUtil redisUtil;
    @Autowired
    private UUserAuthNumService uUserAuthNumService;

    @Override
    public PageRespVO<MintTransferListResp> mintTransferList(PageReqVO pageReqVO) {
        UUser tokenUser = uUserService.getTokenUser();
        PageRespVO<MintTransferListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<MintTransfer> wrapper = new LambdaQueryWrapper<MintTransfer>().eq(MintTransfer::getFromAddress, tokenUser.getUserAddress()).or().eq(MintTransfer::getToAddress, tokenUser.getUserAddress()).orderByDesc(MintTransfer::getCreateTime);
        IPage<MintTransfer> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<MintTransferListResp> respList = new ArrayList<>();
        List<MintTransfer> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (MintTransfer record : records) {
                MintTransferListResp resp = new MintTransferListResp();
                BeanUtils.copyProperties(record, resp);
                resp.setUserAddress(tokenUser.getUserAddress());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public BaseResult<MintTransferResp> transfer(MintTransferReq mintTransferReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUser byUserAddress = uUserService.getByUserAddress(mintTransferReq.getToAddress());
        if (byUserAddress == null) {
            return BaseResult.fail(i18nService.getMessage("20061"));
        }
        String mintMinTransferNum = sSystemConfigService.getByKey(SystemConfigConstant.mint_min_transfer_num).getConfigValue();
        if (mintTransferReq.getMintNum() == null || mintTransferReq.getMintNum().compareTo(new BigInteger(mintMinTransferNum)) < 0) {
            return BaseResult.fail(i18nService.getMessage("20085"));
        }
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        if (mintDeploy == null) {
            return BaseResult.fail(i18nService.getMessage("20081"));
        }
        MintUserHold mintUserHold = mintUserHoldService.getMintUserHold(tokenUser.getUserAddress(), mintDeploy.getId());
        if (mintUserHold.getMintTransferStatus() == 0) {
            return BaseResult.fail(i18nService.getMessage("20093"));
        }
        if (mintUserHold.getMintNum().compareTo(new BigDecimal(mintTransferReq.getMintNum())) < 0) {
            return BaseResult.fail(i18nService.getMessage("20087"));
        }
        String mintJson = "data:,{\"p\":\"denim-20\",\"op\":\"transfer\",\"tick\":\"btia\",\"amt\":\"" + mintTransferReq.getMintNum() + "\"}";
        String mintJsonHex = Hex.stringToHex(mintJson);
        MintTransferResp resp = new MintTransferResp();
        resp.setMintJsonHex(mintJsonHex);
        resp.setToAddress(mintTransferReq.getToAddress());
        UUserAuthNum uUserAuthNum = uUserAuthNumService.refreshAuthNum(tokenUser, 3);
        resp.setAuthNum(uUserAuthNum.getMintTransferAuthNum());
        return BaseResult.success(resp);
    }

    @Override
    public BaseResult<String> transferAuth(MintTransferAuthReq mintTransferAuthReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUserAuthNum authNum = uUserAuthNumService.getAuthNum(mintTransferAuthReq.getAuthNum(), 3);
        if (authNum == null) {
            return BaseResult.fail(i18nService.getMessage("20215"));
        }
        if (authNum.getMintTransferAuthNumStatus() != 1) {
            if (authNum.getMintTransferAuthNumStatus() == 2) {
                return BaseResult.fail(i18nService.getMessage("20301"));
            } else if (authNum.getMintTransferAuthNumStatus() == 3) {
                return BaseResult.fail(i18nService.getMessage("20305"));
            } else if (authNum.getMintTransferAuthNumStatus() == 4) {
                return BaseResult.fail(i18nService.getMessage("20215"));
            } else {
                return BaseResult.fail(i18nService.getMessage("99999"));
            }
        }
        mintUserHoldService.lambdaUpdate()
                .set(MintUserHold::getMintTransferStatus, 0)
                .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                .eq(MintUserHold::getUserAddress, tokenUser.getUserAddress())
                .update();
        uUserAuthNumService.updateAuthNumStatus(tokenUser, 2, 3);
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> transferAuthError(MintTransferAuthErrorReq mintTransferAuthErrorReq) {
        UUser tokenUser = uUserService.getTokenUser();
        long currentTimeMillis = System.currentTimeMillis();
        do {
            UUserAuthNum authNum = uUserAuthNumService.getAuthNum(mintTransferAuthErrorReq.getAuthNum(), 3);
            if (authNum == null) {
                return BaseResult.fail(i18nService.getMessage("20215"));
            }
            if (authNum.getPayBoxAuthNumStatus() != 2) {
                if (authNum.getPayBoxAuthNumStatus() == 1) {
                    if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                        return BaseResult.fail(i18nService.getMessage("20300"));
                    }
                } else if (authNum.getPayBoxAuthNumStatus() == 3) {
                    return BaseResult.fail(i18nService.getMessage("20214"));
                } else if (authNum.getPayBoxAuthNumStatus() == 4) {
                    return BaseResult.fail(i18nService.getMessage("20215"));
                } else {
                    return BaseResult.fail(i18nService.getMessage("99999"));
                }
            } else {
                break;
            }
        } while (true);
        mintUserHoldService.lambdaUpdate()
                .set(MintUserHold::getMintTransferStatus, 1)
                .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                .eq(MintUserHold::getUserAddress, tokenUser.getUserAddress())
                .update();
        uUserAuthNumService.updateAuthNumStatus(tokenUser, 3, 3);
        return BaseResult.success();
    }
}
