package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.NonceUserMintMapper;
import com.nft.cn.entity.NonceUserMint;
import com.nft.cn.service.NonceUserMintService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class NonceUserMintServiceImpl extends ServiceImpl<NonceUserMintMapper, NonceUserMint> implements NonceUserMintService {

    @Override
    public NonceUserMint getByUserAddress(String userAddress) {
        NonceUserMint one = lambdaQuery().eq(NonceUserMint::getUserAddress, userAddress).one();
        if (one == null) {
            one = new NonceUserMint();
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
            NonceUserMint nonceUserMint = getByUserAddress(userAddress);
            Integer nonce = nonceUserMint.getNonce();
            Integer newNonce = nonce + 1;
            boolean update = lambdaUpdate()
                    .set(NonceUserMint::getNonce, newNonce)
                    .set(NonceUserMint::getUpdateTime, LocalDateTime.now())
                    .eq(NonceUserMint::getNonce, nonce)
                    .eq(NonceUserMint::getUserAddress, userAddress)
                    .update();
            if (update) {
                break;
            }
        }
    }
}
