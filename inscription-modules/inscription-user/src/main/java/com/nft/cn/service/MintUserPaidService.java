package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintUserPaid;

import java.util.List;

public interface MintUserPaidService extends IService<MintUserPaid> {

    Integer countPartNum(int phase, List<String> childPartAddress);

}
