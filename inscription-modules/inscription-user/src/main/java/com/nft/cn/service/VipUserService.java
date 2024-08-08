package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.VipUser;
import com.nft.cn.vo.resp.VipUserInfoResp;

public interface VipUserService extends IService<VipUser> {

    VipUser getVipUser(String userAddress);

    VipUserInfoResp info();

    VipUser getByNum(String num);

    VipUserInfoResp communityList();

    VipUserInfoResp refereeList();

}
