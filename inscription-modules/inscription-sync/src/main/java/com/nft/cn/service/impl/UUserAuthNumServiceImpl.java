package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.UUserAuthNumMapper;
import com.nft.cn.entity.MintUser;
import com.nft.cn.entity.MintUserHold;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserAuthNum;
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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UUserAuthNumServiceImpl extends ServiceImpl<UUserAuthNumMapper, UUserAuthNum> implements UUserAuthNumService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private MintTransferService mintTransferService;
    @Autowired
    private SSystemConfigService sSystemConfigService;

    @Override
    public UUserAuthNum getAuthNum(UUser uUser) {
        UUserAuthNum one = null;
        List<UUserAuthNum> uUserAuthNumList = lambdaQuery().eq(UUserAuthNum::getUserAddress, uUser.getUserAddress()).list();
        if (CollectionUtils.isEmpty(uUserAuthNumList)) {
            one = new UUserAuthNum();
            one.setUserId(uUser.getId());
            one.setUserAddress(uUser.getUserAddress());
            one.setCreateTime(LocalDateTime.now());
            one.setUpdateTime(LocalDateTime.now());
            one.setPayBoxAuthNumStatus(0);
            one.setMintAuthNumStatus(0);
            one.setMintTransferAuthNumStatus(0);
            one.setCreateTime(LocalDateTime.now());
            one.setUpdateTime(LocalDateTime.now());
            save(one);
            one = lambdaQuery().eq(UUserAuthNum::getUserAddress, uUser.getUserAddress()).one();
        } else if (uUserAuthNumList.size() > 1){
            baseMapper.removeAuth();
            one = lambdaQuery().eq(UUserAuthNum::getUserAddress, uUser.getUserAddress()).one();
        } else {
            one = lambdaQuery().eq(UUserAuthNum::getUserAddress, uUser.getUserAddress()).one();
        }
        return one;
    }


    @Override
    public UUserAuthNum getAuthNum(String userAddress) {
        UUserAuthNum one = lambdaQuery().eq(UUserAuthNum::getUserAddress, userAddress).one();
        if (one == null) {
            UUser uUser = uUserService.getByUserAddress(userAddress);
            one = new UUserAuthNum();
            one.setUserId(uUser.getId());
            one.setUserAddress(uUser.getUserAddress());
            one.setCreateTime(LocalDateTime.now());
            one.setUpdateTime(LocalDateTime.now());
            one.setPayBoxAuthNumStatus(0);
            one.setMintAuthNumStatus(0);
            one.setMintTransferAuthNumStatus(0);
            save(one);
        }
        return one;
    }

    @Override
    public UUserAuthNum getAuthNum(String authNum, int type) {
        UUserAuthNum one = null;
        if (type == 1) {
            one = lambdaQuery().eq(UUserAuthNum::getPayBoxAuthNum, authNum).one();
        } else if (type == 2) {
            one = lambdaQuery().eq(UUserAuthNum::getMintAuthNum, authNum).one();
        } else if (type == 3) {
            one = lambdaQuery().eq(UUserAuthNum::getMintTransferAuthNum, authNum).one();
        }
        return one;
    }

    @Override
    public UUserAuthNum refreshAuthNum(UUser uUser, long parseLong, int type, String json) {
        UUserAuthNum one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        LocalDateTime localDateTime = now.plusSeconds(parseLong);
        String authNum = UUID.randomUUID().toString().replace("-", "");
        return updateAuth(authNum, type, one, localDateTime, json);
    }


    @Override
    public UUserAuthNum refreshAuthNum(UUser uUser, int type, String json) {
        UUserAuthNum one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        String mintPayDeadlineSecond = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_deadline_second).getConfigValue();
        LocalDateTime localDateTime = now.plusSeconds(Long.parseLong(mintPayDeadlineSecond));
        String authNum = UUID.randomUUID().toString().replace("-", "");
        return updateAuth(authNum, type, one, localDateTime, json);
    }

    @Override
    public UUserAuthNum refreshAuthNum(UUser uUser, long parseLong, int type) {
        UUserAuthNum one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        LocalDateTime localDateTime = now.plusSeconds(parseLong);
        String authNum = UUID.randomUUID().toString().replace("-", "");
        return updateAuth(authNum, type, one, localDateTime, null);
    }


    @Override
    public UUserAuthNum refreshAuthNum(UUser uUser, int type) {
        UUserAuthNum one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        String mintPayDeadlineSecond = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_deadline_second).getConfigValue();
        LocalDateTime localDateTime = now.plusSeconds(Long.parseLong(mintPayDeadlineSecond));
        String authNum = UUID.randomUUID().toString().replace("-", "");
        return updateAuth(authNum, type, one, localDateTime, null);
    }

    @Override
    public UUserAuthNum refreshAuthNum(UUser uUser, String authNum, long parseLong, int type) {
        UUserAuthNum one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        LocalDateTime localDateTime = now.plusSeconds(parseLong);
        return updateAuth(authNum, type, one, localDateTime, null);
    }


    @Override
    public UUserAuthNum refreshAuthNum(UUser uUser, String authNum, int type) {
        UUserAuthNum one = getAuthNum(uUser);
        LocalDateTime now = LocalDateTime.now();
        one.setUpdateTime(now);
        String mintPayDeadlineSecond = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_deadline_second).getConfigValue();
        LocalDateTime localDateTime = now.plusSeconds(Long.parseLong(mintPayDeadlineSecond));
        return updateAuth(authNum, type, one, localDateTime, null);
    }

    @Override
    public void removeAuthNum(String userAddress, int type) {
        LambdaUpdateChainWrapper<UUserAuthNum> wrapper = lambdaUpdate();
        if (type == 1) {
            wrapper.set(UUserAuthNum::getPayBoxAuthNum, null);
            wrapper.set(UUserAuthNum::getPayBoxAuthNumExpire, null);
            wrapper.set(UUserAuthNum::getPayBoxAuthNumJson, null);
            wrapper.set(UUserAuthNum::getPayBoxAuthNumStatus, 0);
        } else if (type == 2) {
            wrapper.set(UUserAuthNum::getMintAuthNum, null);
            wrapper.set(UUserAuthNum::getMintAuthNumExpire, null);
            wrapper.set(UUserAuthNum::getMintAuthNumJson, null);
            wrapper.set(UUserAuthNum::getMintAuthNumStatus, 0);
        } else if (type == 3) {
            wrapper.set(UUserAuthNum::getMintTransferAuthNum, null);
            wrapper.set(UUserAuthNum::getMintTransferAuthNumExpire, null);
            wrapper.set(UUserAuthNum::getMintTransferAuthNumJson, null);
            wrapper.set(UUserAuthNum::getMintTransferAuthNumStatus, 0);
        }
        wrapper.eq(UUserAuthNum::getUserAddress, userAddress).update();
    }

    @Override
    public void updateAuthNumStatus(UUser uUser, int status, int type) {
        UUserAuthNum one = getAuthNum(uUser);
        if (type == 1) {
            one.setPayBoxAuthNumStatus(status);
        } else if (type == 2) {
            one.setMintAuthNumStatus(status);
        } else if (type == 3) {
            one.setMintTransferAuthNumStatus(status);
        }
        updateById(one);
    }


    @Override
    public void updateAuthNumStatus(String userAddress, int status, int type) {
        UUserAuthNum one = getAuthNum(userAddress);
        if (type == 1) {
            one.setPayBoxAuthNumStatus(status);
        } else if (type == 2) {
            one.setMintAuthNumStatus(status);
        } else if (type == 3) {
            one.setMintTransferAuthNumStatus(status);
        }
        updateById(one);
    }

    @Override
    public void userAuthNumStatusScheduled() {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<UUserAuthNum> list = lambdaQuery().eq(UUserAuthNum::getPayBoxAuthNumStatus, 1).lt(UUserAuthNum::getPayBoxAuthNumExpire, localDateTime).list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (UUserAuthNum uUserAuthNum : list) {
            lambdaUpdate().set(UUserAuthNum::getPayBoxAuthNumStatus, 4).set(UUserAuthNum::getUpdateTime, LocalDateTime.now()).eq(UUserAuthNum::getId, uUserAuthNum.getId()).update();
        }
    }

    private UUserAuthNum updateAuth(String authNum, int type, UUserAuthNum one, LocalDateTime localDateTime, String json) {
        if (type == 1) {
            one.setPayBoxAuthNum(authNum);
            one.setPayBoxAuthNumExpire(localDateTime);
            one.setPayBoxAuthNumJson(json);
            one.setPayBoxAuthNumStatus(1);
        } else if (type == 2) {
            one.setMintAuthNum(authNum);
            one.setMintAuthNumExpire(localDateTime);
            one.setMintAuthNumJson(json);
            one.setMintAuthNumStatus(1);
        } else if (type == 3) {
            one.setMintTransferAuthNum(authNum);
            one.setMintTransferAuthNumExpire(localDateTime);
            one.setMintTransferAuthNumJson(json);
            one.setMintTransferAuthNumStatus(1);
        }
        updateById(one);
        return one;
    }

}
