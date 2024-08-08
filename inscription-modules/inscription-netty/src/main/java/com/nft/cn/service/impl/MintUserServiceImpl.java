package com.nft.cn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintUserMapper;
import com.nft.cn.entity.MintUser;
import com.nft.cn.entity.MintUserHold;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserAuthNum;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtil;
import com.nft.cn.vo.req.MintMintAuthErrorReq;
import com.nft.cn.vo.req.MintPayAuthErrorReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MintUserServiceImpl extends ServiceImpl<MintUserMapper, MintUser> implements MintUserService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private MintUserHoldService mintUserHoldService;
    @Autowired
    private UUserAuthNumService uUserAuthNumService;

    @Override
    public BaseResult<String> mintAuthError(String userAddress, MintMintAuthErrorReq mintMintAuthErrorReq) {
        UUserAuthNum authNum = uUserAuthNumService.getAuthNum(mintMintAuthErrorReq.getAuthNum(), 2);
        if (authNum == null) {
            return BaseResult.fail(i18nService.getMessage("20215"));
        }
        if (authNum.getPayBoxAuthNumStatus() != 2) {
            if (authNum.getPayBoxAuthNumStatus() == 1) {
                return BaseResult.fail(i18nService.getMessage("20300"));
            } else if (authNum.getPayBoxAuthNumStatus() == 3) {
                return BaseResult.fail(i18nService.getMessage("20305"));
            } else if (authNum.getPayBoxAuthNumStatus() == 4) {
                return BaseResult.fail(i18nService.getMessage("20215"));
            } else {
                return BaseResult.fail(i18nService.getMessage("99999"));
            }
        }
        MintUser mintUser = null;
        try {
            mintUser = JSONObject.parseObject(authNum.getMintAuthNumJson(), MintUser.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        if (mintUser == null) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        mintUserHoldService.lambdaUpdate()
                .set(MintUserHold::getMintStatus, 2)
                .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                .eq(MintUserHold::getUserAddress, userAddress)
                .update();
        lambdaUpdate()
                .set(MintUser::getStatus, 2)
                .set(MintUser::getUpdateTime, LocalDateTime.now())
                .eq(MintUser::getId, mintUser.getId())
                .update();
        uUserAuthNumService.updateAuthNumStatus(userAddress, 3, 2);
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> payBoxAuthError(String userAddress, MintPayAuthErrorReq mintPayAuthErrorReq) {
        UUserAuthNum authNum = uUserAuthNumService.getAuthNum(mintPayAuthErrorReq.getAuthNum(), 1);
        if (authNum == null) {
            return BaseResult.fail(i18nService.getMessage("20215"));
        }
        if (authNum.getPayBoxAuthNumStatus() != 2) {
            if (authNum.getPayBoxAuthNumStatus() == 1) {
                return BaseResult.fail(i18nService.getMessage("20300"));
            } else if (authNum.getPayBoxAuthNumStatus() == 3) {
                return BaseResult.fail(i18nService.getMessage("20305"));
            } else if (authNum.getPayBoxAuthNumStatus() == 4) {
                return BaseResult.fail(i18nService.getMessage("20215"));
            } else {
                return BaseResult.fail(i18nService.getMessage("99999"));
            }
        }
        MintUser mintUser = null;
        try {
            mintUser = JSONObject.parseObject(authNum.getPayBoxAuthNumJson(), MintUser.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("20218"));
        }
        if (mintUser == null) {
            return BaseResult.fail(i18nService.getMessage("20218"));
        }
        Integer count = lambdaQuery().eq(MintUser::getUserAddress, userAddress).eq(MintUser::getStatus, -1).count();
        int mintStatus = 0;
        if (count != null && count > 0) {
            mintStatus = -1;
        }
        boolean b = removeById(mintUser.getId());
        if (b) {
            mintUserHoldService.lambdaUpdate()
                    .set(MintUserHold::getMintStatus, mintStatus)
                    .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                    .eq(MintUserHold::getUserAddress, userAddress)
                    .update();
            uUserAuthNumService.updateAuthNumStatus(userAddress, 3, 1);
        }
        return BaseResult.success();
    }


}
