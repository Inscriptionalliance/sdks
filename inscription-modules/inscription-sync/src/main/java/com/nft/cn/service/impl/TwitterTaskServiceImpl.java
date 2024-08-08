package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.TwitterTaskMapper;
import com.nft.cn.entity.TwitterTask;
import com.nft.cn.service.TwitterTaskService;
import org.springframework.stereotype.Service;

@Service
public class TwitterTaskServiceImpl extends ServiceImpl<TwitterTaskMapper, TwitterTask> implements TwitterTaskService {

}
