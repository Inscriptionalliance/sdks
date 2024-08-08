package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintDeploy;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.resp.MintDeployInfoResp;

public interface MintDeployService extends IService<MintDeploy> {

    MintDeploy getMintDeploy(String accord);

    BaseResult<MintDeployInfoResp> mintDeployInfo();

}
