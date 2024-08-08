package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.TwitterTaskTokenMapper;
import com.nft.cn.entity.TwitterTaskToken;
import com.nft.cn.service.TwitterTaskTokenService;
import org.springframework.stereotype.Service;

@Service
public class TwitterTaskTokenServiceImpl extends ServiceImpl<TwitterTaskTokenMapper, TwitterTaskToken> implements TwitterTaskTokenService {

}
