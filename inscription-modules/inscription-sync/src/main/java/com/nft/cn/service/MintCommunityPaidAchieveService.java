package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintCommunityPaidAchieve;

public interface MintCommunityPaidAchieveService extends IService<MintCommunityPaidAchieve> {

    void mintCommunityPaidScheduled();

}
