package com.nft.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.cn.entity.MintSwap;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.MintSwapGetPriceReq;
import com.nft.cn.vo.req.MintSwapSwapReq;
import com.nft.cn.vo.resp.MintSwapDrawSwapUsdaResp;
import com.nft.cn.vo.resp.MintSwapGetPriceResp;
import com.nft.cn.vo.resp.MintSwapInfoResp;
import com.nft.cn.vo.resp.MintSwapSwapResp;

public interface MintSwapService extends IService<MintSwap> {

    BaseResult<MintSwapInfoResp> info();

    BaseResult<MintSwapSwapResp> swap(MintSwapSwapReq mintSwapSwapReq);

    BaseResult<MintSwapDrawSwapUsdaResp> drawSwapUsda();

    BaseResult<MintSwapGetPriceResp> getPrice(MintSwapGetPriceReq mintSwapGetPriceReq);

}
