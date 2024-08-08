package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.BridgeHoldBinanceBrc20BtiaMapper;
import com.nft.cn.entity.BridgeHoldBinanceBrc20Btia;
import com.nft.cn.entity.MintUserHold;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.BridgeHoldBinanceBrc20BtiaService;
import com.nft.cn.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BridgeHoldBinanceBrc20BtiaServiceImpl extends ServiceImpl<BridgeHoldBinanceBrc20BtiaMapper, BridgeHoldBinanceBrc20Btia> implements BridgeHoldBinanceBrc20BtiaService {

    @Autowired
    private UUserService uUserService;

    @Override
    public BridgeHoldBinanceBrc20Btia getByUserAddress(String userAddress) {
        BridgeHoldBinanceBrc20Btia one = lambdaQuery().eq(BridgeHoldBinanceBrc20Btia::getUserAddress, userAddress).one();
        if (one == null) {
            UUser byUserAddress = uUserService.getByUserAddress(userAddress);
            if (byUserAddress == null) {
                return null;
            }
            one = new BridgeHoldBinanceBrc20Btia();
            one.setUserId(byUserAddress.getId());
            one.setUserAddress(byUserAddress.getUserAddress());
            one.setLink("Binance");
            one.setAccord("Denim Bridge");
            one.setInscription("BTIA");
            one.setBalance(BigDecimal.ZERO);
            one.setCreateTime(LocalDateTime.now());
            one.setUpdateTime(LocalDateTime.now());
            save(one);
            return lambdaQuery().eq(BridgeHoldBinanceBrc20Btia::getUserAddress, userAddress).one();
        }
        return one;
    }

    @Override
    public boolean updateBalance(String userAddress, BigDecimal amount, Integer operate) {
        while (true) {
            BridgeHoldBinanceBrc20Btia one = getByUserAddress(userAddress);
            if (one == null) {
                return false;
            }
            BigDecimal balanceOld = one.getBalance();
            BigDecimal balanceNew = BigDecimal.ZERO;
            if (operate == 1) {
                balanceNew = balanceOld.add(amount);
            } else {
                balanceNew = balanceOld.subtract(amount);
            }
            if (balanceNew.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(BridgeHoldBinanceBrc20Btia::getBalance, balanceNew)
                    .set(BridgeHoldBinanceBrc20Btia::getUpdateTime, LocalDateTime.now())
                    .eq(BridgeHoldBinanceBrc20Btia::getBalance, balanceOld)
                    .eq(BridgeHoldBinanceBrc20Btia::getId, one.getId())
                    .update();
            if (update) {
                break;
            }
        }
        return true;
    }


}
