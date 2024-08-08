package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.BtiaIncomeMapper;
import com.nft.cn.entity.BtiaIncome;
import com.nft.cn.entity.FreeTransfer;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.BtiaIncomeService;
import com.nft.cn.service.I18nService;
import com.nft.cn.service.MintUserService;
import com.nft.cn.service.UUserService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.Hex;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.req.PledgeIncomeReceiveReq;
import com.nft.cn.vo.resp.FreeTransferListResp;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.PledgeIncomeListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BtiaIncomeServiceImpl extends ServiceImpl<BtiaIncomeMapper, BtiaIncome> implements BtiaIncomeService {


    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintUserService mintUserService;
    @Autowired
    private I18nService i18nService;


    @Override
    public BaseResult<PageRespVO<PledgeIncomeListResp>> pledgeIncomeList(PageReqVO pageReqVO) {
        UUser tokenUser = uUserService.getTokenUser();
        PageRespVO<PledgeIncomeListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<BtiaIncome> wrapper = new LambdaQueryWrapper<BtiaIncome>().eq(BtiaIncome::getUserAddress, tokenUser.getUserAddress()).orderByDesc(BtiaIncome::getCreateTime);
        IPage<BtiaIncome> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<PledgeIncomeListResp> respList = new ArrayList<>();
        List<BtiaIncome> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (BtiaIncome record : records) {
                PledgeIncomeListResp resp = new PledgeIncomeListResp();
                resp.setCreateTime(record.getCreateTime());
                resp.setId(record.getId());
                resp.setAmountRelease(record.getAmountRelease());
                resp.setAmountResidue(record.getAmountResidue());
                resp.setStatus(record.getStatus());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return BaseResult.success(respPageRespVO);
    }

    @Override
    public BaseResult<String> receiveIncome(PledgeIncomeReceiveReq pledgeIncomeReceiveReq) {
        UUser tokenUser = uUserService.getTokenUser();
        BtiaIncome btiaIncome = lambdaQuery().eq(BtiaIncome::getUserAddress, tokenUser.getUserAddress()).eq(BtiaIncome::getId, pledgeIncomeReceiveReq.getId()).one();
        if (btiaIncome == null) {
            return BaseResult.fail(i18nService.getMessage("20404"));
        }
        if (btiaIncome.getStatus() != 0) {
            return BaseResult.fail(i18nService.getMessage("20403"));
        }
        String mintJson = "data:,{\"p\":\"denim-20\",\"op\":\"transfer\",\"tick\":\"btia\",\"amt\":\"" + btiaIncome.getAmountRelease().setScale(0, RoundingMode.DOWN).toPlainString() + "\"}" + btiaIncome.getId();
        String privateKey = "aaa";
        BaseResult<String> stringBaseResult = mintUserService.sendJson(privateKey, tokenUser.getUserAddress(), Hex.stringToHex(mintJson));
        if (stringBaseResult.getCode() == 200) {
            lambdaUpdate().set(BtiaIncome::getStatus, 2).set(BtiaIncome::getReceiveTime, LocalDateTime.now()).set(BtiaIncome::getUpdateTime, LocalDateTime.now()).eq(BtiaIncome::getId, btiaIncome.getId()).update();
        }
        return stringBaseResult;
    }
}
