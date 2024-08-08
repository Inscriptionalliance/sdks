package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.BtiaPledge;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.resp.BtiaPledgeResp;
import com.nft.cn.vo.resp.PledgeInfoResp;
import com.nft.cn.vo.resp.PledgeStatusResp;

public interface BtiaPledgeService extends IService<BtiaPledge> {

    void btiaPledgeAward();
}
