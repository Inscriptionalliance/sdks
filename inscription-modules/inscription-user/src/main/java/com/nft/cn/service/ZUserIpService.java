package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUserReferee;
import com.nft.cn.entity.ZUserIp;

public interface ZUserIpService extends IService<ZUserIp> {

    void saveUserIp(UUserReferee uUserReferee);

}
