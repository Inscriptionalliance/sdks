package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.TwitterTask;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.TaskCheckFulfilReq;

public interface TwitterTaskService extends IService<TwitterTask> {

    BaseResult<String> checkTaskFulfil(TaskCheckFulfilReq taskCheckFulfilReq);
}
