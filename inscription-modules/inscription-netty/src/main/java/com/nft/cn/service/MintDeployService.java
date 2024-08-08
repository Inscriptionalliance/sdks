package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintDeploy;

public interface MintDeployService extends IService<MintDeploy> {

    MintDeploy getMintDeploy(String accord);

}
