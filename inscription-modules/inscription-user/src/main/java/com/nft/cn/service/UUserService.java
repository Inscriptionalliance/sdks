package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUser;
import com.nft.cn.form.LoginPasswordForm;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.UserRefereeNumReq;
import com.nft.cn.vo.resp.UserInfoResp;

import java.util.List;
import java.util.Map;

public interface UUserService extends IService<UUser> {


    UUser getTokenUser();

    BaseResult<Map<String,Object>> loginByPass(LoginPasswordForm form);

    BaseResult<String> userRefereeNum(UUser userByToken, UserRefereeNumReq userRefereeNumReq);

    UUser getByUserAddress(String userAddress);

    UserInfoResp userInfo();

    String maskAddress(String userAddress);

    List<UUser> getChildUser(Long id);

}
