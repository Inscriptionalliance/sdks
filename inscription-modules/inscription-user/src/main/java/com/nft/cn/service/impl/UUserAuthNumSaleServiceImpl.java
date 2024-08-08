package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.UUserAuthNumSaleMapper;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserAuthNum;
import com.nft.cn.entity.UUserAuthNumSale;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.UUID;
import com.nft.cn.vo.req.AuthStatusListReq;
import com.nft.cn.vo.req.MintMintAuthErrorReq;
import com.nft.cn.vo.req.MintPayAuthErrorReq;
import com.nft.cn.vo.req.MintTransferAuthErrorReq;
import com.nft.cn.vo.resp.AuthStatusListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UUserAuthNumSaleServiceImpl extends ServiceImpl<UUserAuthNumSaleMapper, UUserAuthNumSale> implements UUserAuthNumSaleService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private MintTransferService mintTransferService;
    @Autowired
    private SSystemConfigService sSystemConfigService;

    @Override
    public UUserAuthNumSale getAuthNum(UUser uUser) {
        UUserAuthNumSale one = null;
        List<UUserAuthNumSale> uUserAuthNumList = lambdaQuery().eq(UUserAuthNumSale::getUserAddress, uUser.getUserAddress()).list();
        if (CollectionUtils.isEmpty(uUserAuthNumList)) {
            one = new UUserAuthNumSale();
            one.setUserId(uUser.getId());
            one.setUserAddress(uUser.getUserAddress());
            one.setCreateTime(LocalDateTime.now());
            one.setUpdateTime(LocalDateTime.now());
            one.setWithdrawAuthNumStatus(0);
            one.setPayAuthNumStatus(0);
            one.setCreateTime(LocalDateTime.now());
            one.setUpdateTime(LocalDateTime.now());
            save(one);
            one = lambdaQuery().eq(UUserAuthNumSale::getUserAddress, uUser.getUserAddress()).one();
        } else if (uUserAuthNumList.size() > 1){
            baseMapper.removeAuth();
            one = lambdaQuery().eq(UUserAuthNumSale::getUserAddress, uUser.getUserAddress()).one();
        } else {
            one = lambdaQuery().eq(UUserAuthNumSale::getUserAddress, uUser.getUserAddress()).one();
        }
        return one;
    }


    @Override
    public UUserAuthNumSale getAuthNum(String authNum, int type) {
        UUserAuthNumSale one = null;
        if (type == 1) {
            one = lambdaQuery().eq(UUserAuthNumSale::getWithdrawAuthNum, authNum).one();
        } else if (type == 2) {
            one = lambdaQuery().eq(UUserAuthNumSale::getPayAuthNum, authNum).one();
        }
        return one;
    }

    @Override
    public UUserAuthNumSale refreshAuthNum(UUser uUser, long parseLong, int type, String json) {
        UUserAuthNumSale one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        LocalDateTime localDateTime = now.plusSeconds(parseLong);
        String authNum = UUID.randomUUID().toString().replace("-", "");
        return updateAuth(authNum, type, one, localDateTime, json);
    }


    @Override
    public UUserAuthNumSale refreshAuthNum(UUser uUser, int type, String json) {
        UUserAuthNumSale one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        String mintPayDeadlineSecond = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_deadline_second).getConfigValue();
        LocalDateTime localDateTime = now.plusSeconds(Long.parseLong(mintPayDeadlineSecond));
        String authNum = UUID.randomUUID().toString().replace("-", "");
        return updateAuth(authNum, type, one, localDateTime, json);
    }

    @Override
    public UUserAuthNumSale refreshAuthNum(UUser uUser, long parseLong, int type) {
        UUserAuthNumSale one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        LocalDateTime localDateTime = now.plusSeconds(parseLong);
        String authNum = UUID.randomUUID().toString().replace("-", "");
        return updateAuth(authNum, type, one, localDateTime, null);
    }


    @Override
    public UUserAuthNumSale refreshAuthNum(UUser uUser, int type) {
        UUserAuthNumSale one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        String mintPayDeadlineSecond = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_deadline_second).getConfigValue();
        LocalDateTime localDateTime = now.plusSeconds(Long.parseLong(mintPayDeadlineSecond));
        String authNum = UUID.randomUUID().toString().replace("-", "");
        return updateAuth(authNum, type, one, localDateTime, null);
    }

    @Override
    public UUserAuthNumSale refreshAuthNum(UUser uUser, String authNum, long parseLong, int type) {
        UUserAuthNumSale one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        LocalDateTime localDateTime = now.plusSeconds(parseLong);
        return updateAuth(authNum, type, one, localDateTime, null);
    }


    @Override
    public UUserAuthNumSale refreshAuthNum(UUser uUser, String authNum, int type) {
        UUserAuthNumSale one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        String mintPayDeadlineSecond = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_deadline_second).getConfigValue();
        LocalDateTime localDateTime = now.plusSeconds(Long.parseLong(mintPayDeadlineSecond));
        return updateAuth(authNum, type, one, localDateTime, null);
    }

    @Override
    public void removeAuthNum(String userAddress, int type) {
        LambdaUpdateChainWrapper<UUserAuthNumSale> wrapper = lambdaUpdate();
        if (type == 1) {
            wrapper.set(UUserAuthNumSale::getWithdrawAuthNum, null);
            wrapper.set(UUserAuthNumSale::getWithdrawAuthNumExpire, null);
            wrapper.set(UUserAuthNumSale::getWithdrawAuthNumJson, null);
            wrapper.set(UUserAuthNumSale::getWithdrawAuthNumStatus, 0);
        } else if (type == 2) {
            wrapper.set(UUserAuthNumSale::getPayAuthNum, null);
            wrapper.set(UUserAuthNumSale::getPayAuthNumExpire, null);
            wrapper.set(UUserAuthNumSale::getPayAuthNumJson, null);
            wrapper.set(UUserAuthNumSale::getPayAuthNumStatus, 0);
        }
        wrapper.eq(UUserAuthNumSale::getUserAddress, userAddress).update();
    }

    @Override
    public void updateAuthNumStatus(UUser uUser, int status, int type) {
        UUserAuthNumSale one = getAuthNum(uUser);
        if (type == 1) {
            one.setWithdrawAuthNumStatus(status);
        } else if (type == 2) {
            one.setPayAuthNumStatus(status);
        }
        updateById(one);
    }

    private UUserAuthNumSale updateAuth(String authNum, int type, UUserAuthNumSale one, LocalDateTime localDateTime, String json) {
        if (type == 1) {
            one.setWithdrawAuthNum(authNum);
            one.setWithdrawAuthNumExpire(localDateTime);
            one.setWithdrawAuthNumJson(json);
            one.setWithdrawAuthNumStatus(1);
        } else if (type == 2) {
            one.setPayAuthNum(authNum);
            one.setPayAuthNumExpire(localDateTime);
            one.setPayAuthNumJson(json);
            one.setPayAuthNumStatus(1);
        }
        updateById(one);
        return one;
    }
    
}
