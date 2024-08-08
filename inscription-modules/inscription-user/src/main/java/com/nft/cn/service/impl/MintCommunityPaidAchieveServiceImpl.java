package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintCommunityPaidAchieveMapper;
import com.nft.cn.entity.MintCommunityPaidAchieve;
import com.nft.cn.service.MintCommunityPaidAchieveService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MintCommunityPaidAchieveServiceImpl extends ServiceImpl<MintCommunityPaidAchieveMapper, MintCommunityPaidAchieve> implements MintCommunityPaidAchieveService {

    @Override
    public List<MintCommunityPaidAchieve> selectChild(Long userId) {
        return baseMapper.selectChild(userId);
    }

    @Override
    public Integer getAllSubordinateCount(Long userId, Integer phase) {
        return baseMapper.getAllSubordinateCount(userId, phase);
    }
}
