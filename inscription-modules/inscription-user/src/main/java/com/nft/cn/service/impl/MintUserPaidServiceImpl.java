package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintUserPaidMapper;
import com.nft.cn.entity.MintUserPaid;
import com.nft.cn.service.MintUserPaidService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MintUserPaidServiceImpl extends ServiceImpl<MintUserPaidMapper, MintUserPaid> implements MintUserPaidService {

    @Override
    public Integer countPartNum(int phase, List<String> childPartAddress) {
        return baseMapper.countPartNum(phase, childPartAddress);
    }
}
