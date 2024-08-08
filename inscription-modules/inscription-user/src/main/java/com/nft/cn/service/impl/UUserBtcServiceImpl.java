package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UUserBtcMapper;
import com.nft.cn.entity.UUserBtc;
import com.nft.cn.service.UUserBtcService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.BindBtcReq;
import com.nft.cn.vo.req.BtcSetDefaultReq;
import com.nft.cn.vo.req.UnbindBtcReq;
import com.nft.cn.vo.resp.BtcBindListResp;
import com.nft.cn.vo.resp.GetPinNumResp;
import com.nft.cn.vo.resp.PageRespVO;
import org.springframework.stereotype.Service;

@Service
public class UUserBtcServiceImpl extends ServiceImpl<UUserBtcMapper, UUserBtc> implements UUserBtcService {

    @Override
    public BaseResult<GetPinNumResp> getPinNum() {

        return null;
    }

    @Override
    public BaseResult<String> bindBtc(BindBtcReq bindBtcReq) {
        return null;
    }

    @Override
    public BaseResult<String> unbindBtc(UnbindBtcReq unbindBtcReq) {
        return null;
    }

    @Override
    public BaseResult<String> setDefault(BtcSetDefaultReq btcSetDefaultReq) {
        return null;
    }

    @Override
    public BaseResult<PageRespVO<BtcBindListResp>> bindList(BtcSetDefaultReq btcSetDefaultReq) {
        return null;
    }
}
