package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintCommunityPaidAchieve;

import java.util.List;

public interface MintCommunityPaidAchieveService extends IService<MintCommunityPaidAchieve> {

    List<MintCommunityPaidAchieve> selectChild(Long userId);

    Integer getAllSubordinateCount(Long userId, Integer phase);

}
