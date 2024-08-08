package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.NonceSwapMapper;
import com.nft.cn.entity.NonceSwap;
import com.nft.cn.entity.NonceUserMint;
import com.nft.cn.service.NonceSwapService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NonceSwapServiceImpl extends ServiceImpl<NonceSwapMapper, NonceSwap> implements NonceSwapService {



    @Override
    public NonceSwap getByUserAddress(String userAddress) {
        NonceSwap one = lambdaQuery().eq(NonceSwap::getUserAddress, userAddress).one();
        if (one == null) {
            one = new NonceSwap();
            one.setUserAddress(userAddress);
            one.setNonce(0);
            one.setUpdateTime(LocalDateTime.now());
            one.setCreateTime(LocalDateTime.now());
            save(one);
        }
        return one;
    }

    @Override
    public void updateNonce(String userAddress) {
        while (true) {
            NonceSwap nonceUserMint = getByUserAddress(userAddress);
            Integer nonce = nonceUserMint.getNonce();
            Integer newNonce = nonce + 1;
            boolean update = lambdaUpdate()
                    .set(NonceSwap::getNonce, newNonce)
                    .set(NonceSwap::getUpdateTime, LocalDateTime.now())
                    .eq(NonceSwap::getNonce, nonce)
                    .eq(NonceSwap::getUserAddress, userAddress)
                    .update();
            if (update) {
                break;
            }
        }
    }
}
