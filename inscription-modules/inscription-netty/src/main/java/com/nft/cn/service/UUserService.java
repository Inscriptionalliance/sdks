package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUser;

public interface UUserService extends IService<UUser> {


    UUser getTokenUser(String token);

    UUser getTokenUser();

    UUser getByUserAddress(String userAddress);
}
