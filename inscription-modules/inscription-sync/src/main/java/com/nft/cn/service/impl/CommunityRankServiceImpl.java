package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.CommunityRankMapper;
import com.nft.cn.entity.Community;
import com.nft.cn.entity.CommunityRank;
import com.nft.cn.service.CommunityRankService;
import com.nft.cn.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommunityRankServiceImpl extends ServiceImpl<CommunityRankMapper, CommunityRank> implements CommunityRankService {

    @Autowired
    private CommunityService communityService;

    @Override
    public void communityRank(){
        lambdaUpdate().set(CommunityRank::getValid, 0).update();
        List<Community> communityList = communityService.lambdaQuery().orderByDesc(Community::getLikeNum).list();
        long rank = 1;
        for (Community community : communityList) {
            CommunityRank one = lambdaQuery().eq(CommunityRank::getCommunityId, community.getId()).one();
            if (one == null) {
                one = new CommunityRank();
                one.setCommunityId(community.getId());
                one.setCommunityName(community.getName());
                one.setLikeNum(community.getLikeNum());
                one.setRank(rank++);
                one.setValid(1);
                one.setCreateTime(LocalDateTime.now());
                one.setUpdateTime(LocalDateTime.now());
                save(one);
            } else {
                one.setCommunityName(community.getName());
                one.setLikeNum(community.getLikeNum());
                one.setRank(rank++);
                one.setValid(1);
                one.setUpdateTime(LocalDateTime.now());
                updateById(one);
            }
        }
    }


}
