package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintUserHoldMapper;
import com.nft.cn.entity.MintUserHold;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.MintUserHoldService;
import com.nft.cn.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MintUserHoldServiceImpl extends ServiceImpl<MintUserHoldMapper, MintUserHold> implements MintUserHoldService {

    @Autowired
    private UUserService uUserService;

    @Override
    public MintUserHold getMintUserHold(String userAddress, Long deployId) {
        MintUserHold one = lambdaQuery().eq(MintUserHold::getUserAddress, userAddress).eq(MintUserHold::getDeployId, deployId).last("limit 1").one();
        if (one == null) {
            UUser byUserAddress = uUserService.getByUserAddress(userAddress);
            if (byUserAddress == null) {
                return null;
            }
            one = new MintUserHold();
            one.setUserId(byUserAddress.getId());
            one.setUserAddress(byUserAddress.getUserAddress());
            one.setDeployId(deployId);
            one.setMintNum(BigDecimal.ZERO);
            one.setMintStatus(0);
            one.setMintTransferStatus(1);
            one.setCreateTime(LocalDateTime.now());
            one.setUpdateTime(LocalDateTime.now());
            save(one);
            return lambdaQuery().eq(MintUserHold::getUserAddress, userAddress).eq(MintUserHold::getDeployId, deployId).last("limit 1").one();
        }
        return one;
    }

    @Override
    public boolean updateMintNum(String userAddress, Long deployId, BigDecimal mintNum, Integer operate) {
        while (true) {
            MintUserHold mintUserHold = getMintUserHold(userAddress, deployId);
            if (mintUserHold == null) {
                return false;
            }
            BigDecimal mintNumOld = mintUserHold.getMintNum();
            BigDecimal mintNumNew = BigDecimal.ZERO;
            if (operate == 1) {
                mintNumNew = mintNumOld.add(mintNum);
            } else {
                mintNumNew = mintNumOld.subtract(mintNum);
            }
            if (mintNumNew.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(MintUserHold::getMintNum, mintNumNew)
                    .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                    .eq(MintUserHold::getMintNum, mintNumOld)
                    .eq(MintUserHold::getId, mintUserHold.getId())
                    .update();
            if (update) {
                break;
            }
        }
        return true;
    }



    @Override
    public boolean updateUsdaNum(String userAddress, Long deployId, BigDecimal usdaNum, Integer operate) {
        while (true) {
            MintUserHold mintUserHold = getMintUserHold(userAddress, deployId);
            if (mintUserHold == null) {
                return false;
            }
            BigDecimal usdaNumOld = mintUserHold.getUsdaNum();
            BigDecimal usdaNumNew = BigDecimal.ZERO;
            if (operate == 1) {
                usdaNumNew = usdaNumOld.add(usdaNum);
            } else {
                usdaNumNew = usdaNumOld.subtract(usdaNum);
            }
            if (usdaNumNew.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(MintUserHold::getUsdaNum, usdaNumNew)
                    .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                    .eq(MintUserHold::getUsdaNum, usdaNumOld)
                    .eq(MintUserHold::getId, mintUserHold.getId())
                    .update();
            if (update) {
                break;
            }
        }
        return true;
    }


}
