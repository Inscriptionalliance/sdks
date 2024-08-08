package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintCommunityTeamAchieve;

import java.math.BigDecimal;

public interface MintCommunityTeamAchieveService extends IService<MintCommunityTeamAchieve> {

    void mintCommunityScheduled();

}
