package com.nft.cn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.RedisConstant;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.MintHangSaleMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.Hex;
import com.nft.cn.util.LandSignatureHandler;
import com.nft.cn.util.RedisUtil;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MintHangSaleServiceImpl extends ServiceImpl<MintHangSaleMapper, MintHangSale> implements MintHangSaleService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintUserHoldService mintUserHoldService;
    @Autowired
    private MintDeployService mintDeployService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private NonceMarketService nonceMarketService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UUserAuthNumSaleService uUserAuthNumSaleService;
    @Autowired
    private UUserAuthNumService uUserAuthNumService;

    @Value("${chainId}")
    private Integer chainId;
    @Value("${priKey}")
    private String priKey;
    @Value("${marketContractAddress}")
    private String marketContractAddress;

    @Override
    public PageRespVO<MintHangSaleListResp> hangSaleList(MintHangSaleListReq mintHangSaleListReq) {
        PageRespVO<MintHangSaleListResp> respPageRespVO = new PageRespVO<>();
        if (!"Binance".equalsIgnoreCase(mintHangSaleListReq.getChain())) {
            return respPageRespVO;
        }
        LambdaQueryWrapper<MintHangSale> wrapper = new LambdaQueryWrapper<MintHangSale>().eq(MintHangSale::getStatus, 1);
        if (mintHangSaleListReq.getIsMe() != null && mintHangSaleListReq.getIsMe() == 1) {
            UUser tokenUser = uUserService.getTokenUser();
            wrapper.eq(MintHangSale::getUserAddress, tokenUser.getUserAddress());
        }
        if (mintHangSaleListReq.getId() != null && mintHangSaleListReq.getId() > 0) {
            wrapper.eq(MintHangSale::getId, mintHangSaleListReq.getId());
        }
        if (mintHangSaleListReq.getOrder() != null) {
            if (mintHangSaleListReq.getOrder() == 2) {
                wrapper.orderByDesc(MintHangSale::getUnitPrice);
            } else {
                wrapper.orderByAsc(MintHangSale::getUnitPrice);
            }
        }
        IPage<MintHangSale> page = page(new Page<>(mintHangSaleListReq.getPageNum(), mintHangSaleListReq.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<MintHangSaleListResp> respList = new ArrayList<>();
        List<MintHangSale> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> deployIdList = records.stream().map(MintHangSale::getDeployId).collect(Collectors.toList());
            List<MintDeploy> mintDeployList = mintDeployService.lambdaQuery().in(MintDeploy::getId, deployIdList).list();
            for (MintHangSale record : records) {
                MintHangSaleListResp resp = new MintHangSaleListResp();
                List<MintDeploy> collect = mintDeployList.stream().filter(mintDeploy -> mintDeploy.getId().equals(record.getDeployId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(collect)) {
                    MintDeploy mintDeploy = collect.get(0);
                    BeanUtils.copyProperties(mintDeploy, resp);
                    resp.setDeployTime(mintDeploy.getCreateTime());
                }
                BeanUtils.copyProperties(record, resp);
                String mintWithdrawFreeAmount = sSystemConfigService.getByKey(SystemConfigConstant.mint_withdraw_free_amount).getConfigValue();
                String mintPayFreeAmount = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_free_amount).getConfigValue();
                BigDecimal totalPrice = resp.getTotalPrice();
                BigDecimal withdrawFreeAmount = totalPrice.multiply(new BigDecimal(mintWithdrawFreeAmount));
                BigDecimal payFreeAmount = totalPrice.multiply(new BigDecimal(mintPayFreeAmount));
                resp.setWithdrawFreeAmount(withdrawFreeAmount);
                resp.setPayFreeAmount(payFreeAmount);
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public MyMintResp myMint() {
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        if (mintDeploy == null) {
            return null;
        }
        UUser tokenUser = uUserService.getTokenUser();
        MintUserHold mintUserHold = mintUserHoldService.getMintUserHold(tokenUser.getUserAddress(), mintDeploy.getId());
        String mintJson = "data:,{\"p\":\"denim-20\",\"op\":\"mint\",\"tick\":\"btia\",\"amt\":\"" + mintUserHold.getMintNum() + "\"}";
        MyMintResp myMintResp = new MyMintResp();
        myMintResp.setId(mintUserHold.getId());
        myMintResp.setUserAddress(tokenUser.getUserAddress());
        myMintResp.setAccord(accord);
        myMintResp.setMintNum(mintUserHold.getMintNum());
        myMintResp.setMintJson(mintJson);
        myMintResp.setFileType(mintDeploy.getFileType());
        myMintResp.setHash(mintDeploy.getHash());
        myMintResp.setDeployAddress(mintDeploy.getDeployAddress());
        myMintResp.setBlockNum(mintDeploy.getBlockNum());
        myMintResp.setCreateTime(mintDeploy.getCreateTime());
        return myMintResp;
    }

    @Override
    public BaseResult<MintHangSaleResp> hangSale(MintHangSaleReq mintHangSaleReq) {
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        if (mintDeploy == null) {
            return BaseResult.fail(i18nService.getMessage("20081"));
        }
        String mintMinHangSaleTotalPrice = sSystemConfigService.getByKey(SystemConfigConstant.mint_min_hang_sale_total_price).getConfigValue();
        if (mintHangSaleReq.getTotalPrice() == null || mintHangSaleReq.getTotalPrice().compareTo(new BigDecimal(mintMinHangSaleTotalPrice)) < 0) {
            return BaseResult.fail(i18nService.getMessage("20086"));
        }
        String mintMinHangSaleNum = sSystemConfigService.getByKey(SystemConfigConstant.mint_min_hang_sale_num).getConfigValue();
        if (mintHangSaleReq.getMintNum() == null || mintHangSaleReq.getMintNum().compareTo(new BigInteger(mintMinHangSaleNum)) < 0) {
            return BaseResult.fail(i18nService.getMessage("20085"));
        }
        UUser tokenUser = uUserService.getTokenUser();
        MintUserHold mintUserHold = mintUserHoldService.getMintUserHold(tokenUser.getUserAddress(), mintDeploy.getId());
        if (mintUserHold.getMintTransferStatus() == 0) {
            return BaseResult.fail(i18nService.getMessage("20093"));
        }
        if (mintUserHold.getMintNum().compareTo(new BigDecimal(mintHangSaleReq.getMintNum())) < 0) {
            return BaseResult.fail(i18nService.getMessage("20087"));
        }
        String mintJson = "data:,{\"p\":\"denim-20\",\"op\":\"transfer\",\"tick\":\"btia\",\"amt\":\"" + mintHangSaleReq.getMintNum() + "\"}" + mintHangSaleReq.getTotalPrice().toPlainString();
        String mintJsonHex = Hex.stringToHex(mintJson);
        MintHangSaleResp resp = new MintHangSaleResp();
        resp.setMintJsonHex(mintJsonHex);
        String configValue = sSystemConfigService.getByKey(SystemConfigConstant.mint_listing_address).getConfigValue();
        resp.setToAddress(configValue);
        UUserAuthNum uUserAuthNum = uUserAuthNumService.refreshAuthNum(tokenUser, 3, null);
        resp.setAuthNum(uUserAuthNum.getMintTransferAuthNum());
        return BaseResult.success(resp);
    }

    @Override
    public BaseResult<MintWithdrawResp> withdraw(MintWithdrawReq mintWithdrawReq) {
        MintHangSale mintHangSale = getById(mintWithdrawReq.getId());
        if (mintHangSale == null) {
            return BaseResult.fail(i18nService.getMessage("20088"));
        }
        if (mintHangSale.getStatus() == 0 || mintHangSale.getStatus() == 3) {
            return BaseResult.fail(i18nService.getMessage("20089"));
        }
        if (mintHangSale.getStatus() == 2 || mintHangSale.getStatus() == 4) {
            return BaseResult.fail(i18nService.getMessage("20090"));
        }
        UUser tokenUser = uUserService.getTokenUser();
        if (!tokenUser.getUserAddress().equalsIgnoreCase(mintHangSale.getUserAddress())) {
            return BaseResult.fail(i18nService.getMessage("20092"));
        }
        String drawLockKey = redisUtil.getString(RedisConstant.USER_MARKET_TRANSFER_LOCK + mintHangSale.getUserAddress());
        if (drawLockKey != null) {
            return BaseResult.fail(i18nService.getMessage("20095"));
        }
        String enStr = withdrawGetEnStr(mintHangSale);
        String drawLockTime = sSystemConfigService.getByKey(SystemConfigConstant.draw_lock_time).getConfigValue();
        redisUtil.setString(RedisConstant.USER_MARKET_TRANSFER_LOCK + mintHangSale.getUserAddress(), mintHangSale.getId().toString(), Long.parseLong(drawLockTime));
        MintWithdrawResp resp = new MintWithdrawResp();
        resp.setSign(enStr);
        resp.setAmount(BigDecimal.ZERO);
        String mintWithdrawFreeAmount = sSystemConfigService.getByKey(SystemConfigConstant.mint_withdraw_free_amount).getConfigValue();
        resp.setFree(new BigDecimal(mintWithdrawFreeAmount).multiply(mintHangSale.getTotalPrice()).multiply(new BigDecimal("10").pow(18)).setScale(0, RoundingMode.DOWN));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", mintHangSale.getId());
        UUserAuthNumSale uUserAuthNum = uUserAuthNumSaleService.refreshAuthNum(tokenUser, 1, jsonObject.toJSONString());
        resp.setAuthNum(uUserAuthNum.getWithdrawAuthNum());
        return BaseResult.success(resp);
    }

    @Override
    public BaseResult<MintPayResp> pay(MintPayReq mintPayReq) {
        MintHangSale mintHangSale = getById(mintPayReq.getId());
        if (mintHangSale == null) {
            return BaseResult.fail(i18nService.getMessage("20088"));
        }
        if (mintHangSale.getStatus() == 0 || mintHangSale.getStatus() == 3) {
            return BaseResult.fail(i18nService.getMessage("20089"));
        }
        if (mintHangSale.getStatus() == 2 || mintHangSale.getStatus() == 4) {
            return BaseResult.fail(i18nService.getMessage("20090"));
        }
        UUser tokenUser = uUserService.getTokenUser();
        if (tokenUser.getUserAddress().equalsIgnoreCase(mintHangSale.getUserAddress())) {
            return BaseResult.fail(i18nService.getMessage("20091"));
        }
        String drawLockKey = redisUtil.getString(RedisConstant.USER_MARKET_TRANSFER_LOCK + mintHangSale.getUserAddress());
        if (drawLockKey != null) {
            return BaseResult.fail(i18nService.getMessage("20095"));
        }
        String enStr = payGetEnStr(mintHangSale, tokenUser.getUserAddress());
        String drawLockTime = sSystemConfigService.getByKey(SystemConfigConstant.draw_lock_time).getConfigValue();
        redisUtil.setString(RedisConstant.USER_MARKET_TRANSFER_LOCK + mintHangSale.getUserAddress(), mintHangSale.getId().toString(), Long.parseLong(drawLockTime));
        MintPayResp resp = new MintPayResp();
        resp.setSign(enStr);
        resp.setAmount(mintHangSale.getTotalPrice());
        String mintPayFreeAmount = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_free_amount).getConfigValue();
        BigDecimal freeAmount = new BigDecimal(mintPayFreeAmount).multiply(mintHangSale.getTotalPrice()).multiply(new BigDecimal("10").pow(18)).setScale(0, RoundingMode.DOWN);
        resp.setFree(freeAmount);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", mintHangSale.getId());
        UUserAuthNumSale uUserAuthNum = uUserAuthNumSaleService.refreshAuthNum(tokenUser, 1, jsonObject.toJSONString());
        resp.setAuthNum(uUserAuthNum.getWithdrawAuthNum());
        return BaseResult.success(resp);
    }

    @Override
    public BaseResult<String> withdrawAuth(MintAuthReq mintAuthReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUserAuthNumSale authNum = uUserAuthNumSaleService.getAuthNum(mintAuthReq.getAuthNum(), 1);
        if (authNum == null) {
            return BaseResult.fail(i18nService.getMessage("20215"));
        }
        if (authNum.getWithdrawAuthNumStatus() != 1) {
            if (authNum.getWithdrawAuthNumStatus() == 2) {
                return BaseResult.fail(i18nService.getMessage("20405"));
            } else if (authNum.getWithdrawAuthNumStatus() == 3) {
                return BaseResult.fail(i18nService.getMessage("20217"));
            } else if (authNum.getWithdrawAuthNumStatus() == 4) {
                return BaseResult.fail(i18nService.getMessage("20215"));
            } else {
                return BaseResult.fail(i18nService.getMessage("99999"));
            }
        }
        MintHangSale mintHangSale = null;
        try {
            mintHangSale = JSONObject.parseObject(authNum.getWithdrawAuthNumJson(), MintHangSale.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        if (mintHangSale == null) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        lambdaUpdate()
                .set(MintHangSale::getStatus, 3)
                .set(MintHangSale::getUpdateTime, LocalDateTime.now())
                .eq(MintHangSale::getStatus, 1)
                .eq(MintHangSale::getId, mintHangSale.getId())
                .update();
        uUserAuthNumSaleService.updateAuthNumStatus(tokenUser, 2, 1);
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> withdrawAuthError(MintAuthReq mintAuthReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUserAuthNumSale uUserAuthNumSale = null;
        long currentTimeMillis = System.currentTimeMillis();
        do {
            uUserAuthNumSale = uUserAuthNumSaleService.getAuthNum(mintAuthReq.getAuthNum(), 1);
            if (uUserAuthNumSale == null) {
                if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                    return BaseResult.fail(i18nService.getMessage("20215"));
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
            }
            if (uUserAuthNumSale.getWithdrawAuthNumStatus() != 2) {
                if (uUserAuthNumSale.getWithdrawAuthNumStatus() == 1) {
                    if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                        return BaseResult.fail(i18nService.getMessage("20300"));
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
            } else {
                break;
            }
        } while (true);

        MintHangSale mintHangSale = null;
        try {
            mintHangSale = JSONObject.parseObject(uUserAuthNumSale.getWithdrawAuthNumJson(), MintHangSale.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        if (mintHangSale == null) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        MintHangSale byId = getById(mintHangSale.getId());
        if (byId.getStatus() == 1 || byId.getStatus() == 3) {
            boolean update = lambdaUpdate()
                    .set(MintHangSale::getStatus, 1)
                    .set(MintHangSale::getUpdateTime, LocalDateTime.now())
                    .eq(MintHangSale::getStatus, 3)
                    .eq(MintHangSale::getId, byId.getId())
                    .update();
        }
        uUserAuthNumSaleService.updateAuthNumStatus(tokenUser, 3, 1);
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> payAuth(MintAuthReq mintAuthReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUserAuthNumSale authNum = uUserAuthNumSaleService.getAuthNum(mintAuthReq.getAuthNum(), 2);
        if (authNum == null) {
            return BaseResult.fail(i18nService.getMessage("20215"));
        }
        if (authNum.getWithdrawAuthNumStatus() != 1) {
            if (authNum.getWithdrawAuthNumStatus() == 2) {
                return BaseResult.fail(i18nService.getMessage("20405"));
            } else if (authNum.getWithdrawAuthNumStatus() == 3) {
                return BaseResult.fail(i18nService.getMessage("20217"));
            } else if (authNum.getWithdrawAuthNumStatus() == 4) {
                return BaseResult.fail(i18nService.getMessage("20215"));
            } else {
                return BaseResult.fail(i18nService.getMessage("99999"));
            }
        }
        MintHangSale mintHangSale = null;
        try {
            mintHangSale = JSONObject.parseObject(authNum.getPayAuthNumJson(), MintHangSale.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        if (mintHangSale == null) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        lambdaUpdate()
                .set(MintHangSale::getStatus, 4)
                .set(MintHangSale::getUpdateTime, LocalDateTime.now())
                .eq(MintHangSale::getStatus, 1)
                .eq(MintHangSale::getId, mintHangSale.getId())
                .update();
        uUserAuthNumSaleService.updateAuthNumStatus(tokenUser, 2, 2);
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> payAuthError(MintAuthReq mintAuthReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUserAuthNumSale uUserAuthNumSale = null;
        long currentTimeMillis = System.currentTimeMillis();
        do {
            uUserAuthNumSale = uUserAuthNumSaleService.getAuthNum(mintAuthReq.getAuthNum(), 2);
            if (uUserAuthNumSale == null) {
                if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                    return BaseResult.fail(i18nService.getMessage("20215"));
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
            }
            if (uUserAuthNumSale.getPayAuthNumStatus() != 2) {
                if (uUserAuthNumSale.getPayAuthNumStatus() == 1) {
                    if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                        return BaseResult.fail(i18nService.getMessage("20300"));
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
            } else {
                break;
            }
        } while (true);

        MintHangSale mintHangSale = null;
        try {
            mintHangSale = JSONObject.parseObject(uUserAuthNumSale.getPayAuthNumJson(), MintHangSale.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        if (mintHangSale == null) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        MintHangSale byId = getById(mintHangSale.getId());
        if (byId.getStatus() == 1 || byId.getStatus() == 4) {
            boolean update = lambdaUpdate()
                    .set(MintHangSale::getStatus, 1)
                    .set(MintHangSale::getUpdateTime, LocalDateTime.now())
                    .eq(MintHangSale::getStatus, 4)
                    .eq(MintHangSale::getId, byId.getId())
                    .update();
        }
        uUserAuthNumSaleService.updateAuthNumStatus(tokenUser, 3, 2);
        return BaseResult.success();
    }

    public String withdrawGetEnStr(MintHangSale mintHangSale) {
        return "";
    }

    private String payGetEnStr(MintHangSale mintHangSale, String userAddress) {
        return "";
    }
}
