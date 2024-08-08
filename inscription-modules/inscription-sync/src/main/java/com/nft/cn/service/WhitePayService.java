package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.WhitePay;

public interface WhitePayService extends IService<WhitePay> {

    void syncPayWhite(String s, String hash);

}
