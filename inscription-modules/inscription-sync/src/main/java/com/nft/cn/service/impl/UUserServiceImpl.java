package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UUserMapper;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.UUserService;
import org.springframework.stereotype.Service;

@Service
public class UUserServiceImpl extends ServiceImpl<UUserMapper, UUser> implements UUserService {

    @Override
    public UUser getByUserAddress(String userAddress) {
        return lambdaQuery().eq(UUser::getUserAddress, userAddress).one();
    }

}
