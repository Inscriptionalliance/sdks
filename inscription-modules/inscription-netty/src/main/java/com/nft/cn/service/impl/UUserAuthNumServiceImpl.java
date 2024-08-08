package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.UUserAuthNumMapper;
import com.nft.cn.entity.*;
import com.nft.cn.netty.Session.Session;
import com.nft.cn.netty.Session.SessionFactory;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtils;
import com.nft.cn.util.UUID;
import com.nft.cn.vo.req.AuthStatusListReq;
import com.nft.cn.vo.req.MintMintAuthErrorReq;
import com.nft.cn.vo.req.MintPayAuthErrorReq;
import com.nft.cn.vo.req.MintTransferAuthErrorReq;
import com.nft.cn.vo.resp.AuthStatusListResp;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UUserAuthNumServiceImpl extends ServiceImpl<UUserAuthNumMapper, UUserAuthNum> implements UUserAuthNumService {

    @Autowired
    private MintUserHoldService mintUserHoldService;
    @Autowired
    private MintDeployService mintDeployService;
    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintTransferService mintTransferService;
    @Autowired
    private SSystemConfigService sSystemConfigService;

    @Override
    public void replaceAuthNumStatus() {
        Session session = SessionFactory.getSession();
        List<String> userAddressList = session.getUserAddressList();
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        List<MintUserHold> mintUserHoldList = mintUserHoldService.lambdaQuery().eq(MintUserHold::getDeployId, mintDeploy.getId()).in(MintUserHold::getUserAddress, userAddressList).list();
        List<MintUser> mintUserList = mintUserService.lambdaQuery().eq(MintUser::getStatus, 2).in(MintUser::getUserAddress, userAddressList).list();
        List<UUserAuthNum> uUserAuthNumList = lambdaQuery().in(UUserAuthNum::getUserAddress, userAddressList).list();
        session.setMintUserHoldList(mintUserHoldList);
        session.setMintUserList(mintUserList);
        session.setUUserAuthNumList(uUserAuthNumList);
    }

    @Override
    public UUserAuthNum getAuthNum(UUser uUser) {
        UUserAuthNum one = lambdaQuery().eq(UUserAuthNum::getUserAddress, uUser.getUserAddress()).one();
        if (one == null) {
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
        updateAuthNumStatus(status, type, getAuthNum(uUser));
    }

    @Override
    public void updateAuthNumStatus(String userAddress, int status, int type) {
        updateAuthNumStatus(status, type, getAuthNum(userAddress));
    }

    private void updateAuthNumStatus(int status, int type, UUserAuthNum authNum) {
        if (type == 1) {
            authNum.setPayBoxAuthNumStatus(status);
        } else if (type == 2) {
            authNum.setMintAuthNumStatus(status);
        } else if (type == 3) {
            authNum.setMintTransferAuthNumStatus(status);
        }
        updateById(authNum);
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
