package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintUserBoxMapper;
import com.nft.cn.entity.MintUserBox;
import com.nft.cn.service.MintUserBoxService;
import org.springframework.stereotype.Service;

@Service
public class MintUserBoxServiceImpl extends ServiceImpl<MintUserBoxMapper, MintUserBox> implements MintUserBoxService {

    @Override
    public MintUserBox getByBoxNum(String boxNum) {
        return lambdaQuery().eq(MintUserBox::getNum, boxNum).one();
    }

}
