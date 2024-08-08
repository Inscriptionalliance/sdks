package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.NonceBridgeMapper;
import com.nft.cn.entity.NonceBridge;
import com.nft.cn.entity.NonceMarket;
import com.nft.cn.service.NonceBridgeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NonceBridgeServiceImpl extends ServiceImpl<NonceBridgeMapper, NonceBridge> implements NonceBridgeService {


    @Override
    public NonceBridge getByUserAddress(String userAddress) {
        NonceBridge one = lambdaQuery().eq(NonceBridge::getUserAddress, userAddress).one();
        if (one == null) {
            one = new NonceBridge();
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
            NonceBridge nonceUserMint = getByUserAddress(userAddress);
            Integer nonce = nonceUserMint.getNonce();
            Integer newNonce = nonce + 1;
            boolean update = lambdaUpdate()
                    .set(NonceBridge::getNonce, newNonce)
                    .set(NonceBridge::getUpdateTime, LocalDateTime.now())
                    .eq(NonceBridge::getNonce, nonce)
                    .eq(NonceBridge::getUserAddress, userAddress)
                    .update();
            if (update) {
                break;
            }
        }
    }

}
