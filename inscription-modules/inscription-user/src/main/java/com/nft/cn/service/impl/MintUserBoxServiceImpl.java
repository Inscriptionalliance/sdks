package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintUserBoxMapper;
import com.nft.cn.entity.MintUserBox;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.MintUserBoxService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class MintUserBoxServiceImpl extends ServiceImpl<MintUserBoxMapper, MintUserBox> implements MintUserBoxService {

    @Autowired
    private UUserService uUserService;

    @Override
    public BaseResult<MintUserBoxInfoResp> info() {
        UUser tokenUser = uUserService.getTokenUser();
        MintUserBoxInfoResp resp = new MintUserBoxInfoResp();
        MintUserBox one = lambdaQuery().eq(MintUserBox::getUserAddress, tokenUser.getUserAddress()).one();
        if (one != null) {
            BeanUtils.copyProperties(one, resp);
            resp.setIsMintBox(1);
        } else {
            resp.setIsMintBox(0);
        }
        return BaseResult.success(resp);
    }

    @Override
    public PageRespVO<BoxBridgeBalanceListResp> bridgeBalanceList(BoxBridgeBalanceListReq boxBridgeBalanceListReq) {
        return null;
    }

    @Override
    public BaseResult<MintUserBoxBridgeResp> bridge(MintUserBoxBridgeReq mintUserBoxBridgeReq) {
        return null;
    }

    @Override
    public BaseResult<MintUserBoxBridgePayResp> bridgePay(MintUserBoxBridgePayReq mintUserBoxBridgePayReq) {
        return null;
    }

    @Override
    public BaseResult<MintUserBoxBridgePriceResp> getTargetNum(MintUserBoxBridgePriceReq mintUserBoxBridgePriceReq) {
        MintUserBoxBridgePriceResp resp = new MintUserBoxBridgePriceResp();
        resp.setFree(BigInteger.ZERO);
        resp.setTargetNum(mintUserBoxBridgePriceReq.getNum());
        return BaseResult.success(resp);
    }

    @Override
    public MintUserBox getByBoxNum(String boxNum) {
        return lambdaQuery().eq(MintUserBox::getNum, boxNum).one();
    }

    @Override
    public PageRespVO<BridgeRecordListResp> bridgeRecordList(BridgeRecordListReq bridgeRecordListReq) {
        return null;
    }

}
