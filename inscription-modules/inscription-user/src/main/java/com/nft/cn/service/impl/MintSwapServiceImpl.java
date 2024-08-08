package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.RedisConstant;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.MintSwapMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.Hex;
import com.nft.cn.util.LandSignatureHandler;
import com.nft.cn.util.RedisUtil;
import com.nft.cn.vo.req.MintSwapGetPriceReq;
import com.nft.cn.vo.req.MintSwapSwapReq;
import com.nft.cn.vo.resp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MintSwapServiceImpl extends ServiceImpl<MintSwapMapper, MintSwap> implements MintSwapService {

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
    private SSystemStatisticsService sSystemStatisticsService;


    @Override
    public BaseResult<MintSwapInfoResp> info() {
        UUser tokenUser = uUserService.getTokenUser();
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        if (mintDeploy == null) {
            return BaseResult.fail(i18nService.getMessage("20081"));
        }
        MintUserHold mintUserHold = mintUserHoldService.getMintUserHold(tokenUser.getUserAddress(), mintDeploy.getId());
        String mintSwapFreeAmount = sSystemConfigService.getByKey(SystemConfigConstant.mint_swap_free_amount).getConfigValue();
        String mintSwapMinNum = sSystemConfigService.getByKey(SystemConfigConstant.mint_swap_min_num).getConfigValue();
        MintSwapInfoResp resp = new MintSwapInfoResp();
        resp.setUserAddress(tokenUser.getUserAddress());
        resp.setMintSwapFreeAmount(new BigDecimal(mintSwapFreeAmount));
        resp.setMintNum(mintUserHold.getMintNum());
        resp.setMintSwapMinNum(new BigInteger(mintSwapMinNum));
        return BaseResult.success(resp);
    }

    @Override
    public BaseResult<MintSwapSwapResp> swap(MintSwapSwapReq mintSwapSwapReq) {
        return BaseResult.success();
    }

    @Override
    public BaseResult<MintSwapDrawSwapUsdaResp> drawSwapUsda() {
        return BaseResult.success();
    }

    @Override
    public BaseResult<MintSwapGetPriceResp> getPrice(MintSwapGetPriceReq mintSwapGetPriceReq) {
        if (mintSwapGetPriceReq.getMintNum().compareTo(BigInteger.ZERO) <= 0) {
            return BaseResult.fail(i18nService.getMessage("20111"));
        }
        BigDecimal price = getPrice(mintSwapGetPriceReq.getMintNum(), mintSwapGetPriceReq.getType());
        MintSwapGetPriceResp resp = new MintSwapGetPriceResp();
        resp.setPrice(price);
        return BaseResult.success(resp);
    }

    public BigDecimal getPrice(BigInteger mintNum, Integer type) {
        String mintPricePoolCoefficient = sSystemConfigService.getByKey(SystemConfigConstant.mint_price_pool_coefficient).getConfigValue();
        BigDecimal coefficient = new BigDecimal(mintPricePoolCoefficient);
        BigDecimal poolMintNum = new BigDecimal(sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_price_pool_mint_num).getStatisticsValue());
        BigDecimal poolUsdaNum = new BigDecimal(sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_price_pool_usda_num).getStatisticsValue());
        BigDecimal price = null;
        if (type == 1) {
            price = poolUsdaNum.subtract(coefficient.divide(poolMintNum.add(new BigDecimal(mintNum)), 18, RoundingMode.DOWN));
        } else if (type == 0) {
            price = coefficient.divide(poolMintNum.subtract(new BigDecimal(mintNum)), 18, RoundingMode.DOWN).subtract(poolUsdaNum);
        }
        return price;
    }


    public BaseResult<MintSwapSwapResp> swapUSDA(MintSwapSwapReq mintSwapSwapReq, MintDeploy mintDeploy, MintUserHold mintUserHold) {
        return BaseResult.success();
    }

    public BaseResult<MintSwapSwapResp> swapMint(MintSwapSwapReq mintSwapSwapReq, MintDeploy mintDeploy, MintUserHold mintUserHold) {
        return BaseResult.success();
    }


    private String swapGetEnStr(String userAddress, Integer type, BigDecimal price, Long order) {
        return "";
    }

}
