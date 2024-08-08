package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserAuthNum;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.AuthStatusListReq;
import com.nft.cn.vo.resp.AuthStatusListResp;

public interface UUserAuthNumService extends IService<UUserAuthNum> {


    UUserAuthNum getAuthNum(UUser uUser);

    UUserAuthNum getAuthNum(String userAddress);

    UUserAuthNum getAuthNum(String authNum, int type);

    UUserAuthNum refreshAuthNum(UUser uUser, long parseLong, int type, String json);
    UUserAuthNum refreshAuthNum(UUser uUser, int type, String json);
    UUserAuthNum refreshAuthNum(UUser uUser, long parseLong, int type);
    UUserAuthNum refreshAuthNum(UUser uUser, int type);
    UUserAuthNum refreshAuthNum(UUser uUser, String authNum, long parseLong, int type);
    UUserAuthNum refreshAuthNum(UUser uUser, String authNum, int type);


    void removeAuthNum(String userAddress, int type);

    void updateAuthNumStatus(UUser uUser, int status, int type);


    void updateAuthNumStatus(String userAddress, int status, int type);

    void userAuthNumStatusScheduled();

}
