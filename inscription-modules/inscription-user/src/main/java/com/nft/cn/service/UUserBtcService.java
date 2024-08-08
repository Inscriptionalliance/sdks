package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.UUserBtc;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.BindBtcReq;
import com.nft.cn.vo.req.BtcSetDefaultReq;
import com.nft.cn.vo.req.UnbindBtcReq;
import com.nft.cn.vo.resp.BtcBindListResp;
import com.nft.cn.vo.resp.GetPinNumResp;
import com.nft.cn.vo.resp.PageRespVO;

public interface UUserBtcService extends IService<UUserBtc> {

    BaseResult<GetPinNumResp> getPinNum();

    BaseResult<String> bindBtc(BindBtcReq bindBtcReq);

    BaseResult<String> unbindBtc(UnbindBtcReq unbindBtcReq);

    BaseResult<String> setDefault(BtcSetDefaultReq btcSetDefaultReq);

    BaseResult<PageRespVO<BtcBindListResp>> bindList(BtcSetDefaultReq btcSetDefaultReq);

}
