package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserAuthNum;
import com.nft.cn.entity.UUserAuthNumSale;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.AuthStatusListReq;
import com.nft.cn.vo.resp.AuthStatusListResp;

public interface UUserAuthNumSaleService extends IService<UUserAuthNumSale> {

    UUserAuthNumSale getAuthNum(UUser uUser);

    UUserAuthNumSale getAuthNum(String authNum, int type);

    UUserAuthNumSale refreshAuthNum(UUser uUser, long parseLong, int type, String json);
    UUserAuthNumSale refreshAuthNum(UUser uUser, int type, String json);
    UUserAuthNumSale refreshAuthNum(UUser uUser, long parseLong, int type);
    UUserAuthNumSale refreshAuthNum(UUser uUser, int type);
    UUserAuthNumSale refreshAuthNum(UUser uUser, String authNum, long parseLong, int type);
    UUserAuthNumSale refreshAuthNum(UUser uUser, String authNum, int type);


    void removeAuthNum(String userAddress, int type);

    void updateAuthNumStatus(UUser uUser, int status, int type);


}
