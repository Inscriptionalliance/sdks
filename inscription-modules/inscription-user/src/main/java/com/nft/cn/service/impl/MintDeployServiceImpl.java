package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintDeployMapper;
import com.nft.cn.dao.MintUserMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.resp.MintDeployInfoResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class MintDeployServiceImpl extends ServiceImpl<MintDeployMapper, MintDeploy> implements MintDeployService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintRankService mintRankService;
    @Autowired
    private MintUserHoldService mintUserHoldService;
    @Autowired
    private MintTransferService mintTransferService;
    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private MintUserBoxService mintUserBoxService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MintUserMapper mintUserMapper;

    @Override
    public MintDeploy getMintDeploy(String accord) {
        return lambdaQuery().eq(MintDeploy::getAccord, accord).one();
    }

    @Override
    public BaseResult<MintDeployInfoResp> mintDeployInfo() {
        UUser tokenUser = uUserService.getTokenUser();
        List<MintDeploy> list = lambdaQuery().list();
        MintDeploy mintDeploy = list.get(0);
        MintDeployInfoResp resp = new MintDeployInfoResp();
        BeanUtils.copyProperties(mintDeploy, resp);
        Page<MintRankUser> page = new Page<>(1, 99);
        page = mintUserMapper.selectMintRankPage(page, tokenUser.getUserAddress());
        List<MintRankUser> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            MintRankUser mintRankUser = records.get(0);
            resp.setRank(String.valueOf(mintRankUser.getRank()));
        }
        if (StringUtils.isEmpty(resp.getRank())) {
            resp.setRank("999+");
        }
        String formattedNumber = formatNumberWithCommas(String.valueOf(mintDeploy.getMintAll().longValue()));
        resp.setMintAll(formattedNumber);
        MintUserHold mintUserHold = mintUserHoldService.getMintUserHold(tokenUser.getUserAddress(), mintDeploy.getId());
        resp.setMyMintNum(mintUserHold.getMintNum().divide(new BigDecimal("10000"), 2));
        resp.setIsAllowMint(mintUserHold.getMintStatus());
        MintUser mintUser = mintUserService.getOne(new QueryWrapper<MintUser>().select("sum(mint_num) as mintNum").eq("status", 4));
        if (mintUser != null) {
            BigDecimal castRatio = mintUser.getMintNum().multiply(new BigDecimal("100")).divide(mintDeploy.getMintAll().multiply(mintDeploy.getMintOne()), 2, RoundingMode.DOWN);
            resp.setCastRatio(castRatio.toPlainString() + "%");
            resp.setMintNum(mintUser.getMintNum().divide(new BigDecimal("10000"), 2));
        } else {
            resp.setCastRatio("0%");
            resp.setMintNum(BigDecimal.ZERO);
        }
        Integer count = mintUserHoldService.lambdaQuery().gt(MintUserHold::getMintNum, BigDecimal.ZERO).eq(MintUserHold::getDeployId, mintDeploy.getId()).count();
        if (count != null) {
            resp.setPeopleNum(count);
        } else {
            resp.setPeopleNum(0);
        }
        MintTransfer status = mintTransferService.getOne(new QueryWrapper<MintTransfer>().select("sum(mint_num) as mintNum"));
        if (status != null) {
            resp.setTradeAmount(status.getMintNum().divide(new BigDecimal("10000"), 2));
        } else {
            resp.setTradeAmount(BigDecimal.ZERO);
        }
        MintUserBox one = mintUserBoxService.lambdaQuery().eq(MintUserBox::getUserAddress, tokenUser.getUserAddress()).one();
        if (one != null) {
            resp.setIsMintBox(1);
        } else {
            resp.setIsMintBox(0);
        }
        return BaseResult.success(resp);
    }


    public static String formatNumberWithCommas(String number) {
        String reversedNumber = new StringBuilder(number).reverse().toString();
        String s = String.join(",",
                splitStringIntoChunks(reversedNumber, 3)).replaceAll(",$", "");
        return new StringBuilder(s).reverse().toString();
    }

    public static List<String> splitStringIntoChunks(String str, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < str.length(); i += chunkSize) {
            chunks.add(str.substring(i, Math.min(str.length(), i + chunkSize)));
        }
        return chunks;
    }

}
