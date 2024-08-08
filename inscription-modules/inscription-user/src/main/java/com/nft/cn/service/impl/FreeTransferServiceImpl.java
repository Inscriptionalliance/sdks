package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.FreeTransferMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.FreeTransferReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.FreeTransferListResp;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.WhitePayListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FreeTransferServiceImpl extends ServiceImpl<FreeTransferMapper, FreeTransfer> implements FreeTransferService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private UUserRefereeService uUserRefereeService;
    @Autowired
    private MintUserService mintUserService;

    @Override
    public BaseResult<String> transfer(FreeTransferReq freeTransferReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(tokenUser.getUserAddress());
        if (freeTransferReq.getFreeNum().compareTo(BigInteger.ZERO) <= 0) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        BigDecimal mintFreeTicket = BigDecimal.ZERO;
        List<MintUser> mintUserList = mintUserService.lambdaQuery().eq(MintUser::getUserAddress, tokenUser.getUserAddress()).in(MintUser::getStatus, 1).list();
        if (!CollectionUtils.isEmpty(mintUserList)) {
            for (MintUser mintUser : mintUserList) {
                mintFreeTicket = mintFreeTicket.add(mintUser.getFreeTicket());
            }
        }
        if ((uUserBaseInfo.getFreeTicket().subtract(mintFreeTicket)).compareTo(new BigDecimal(freeTransferReq.getFreeNum())) < 0) {
            return BaseResult.fail(i18nService.getMessage("20060"));
        }
        UUser byUserAddress = uUserService.getByUserAddress(freeTransferReq.getToAddress().toLowerCase());
        if (byUserAddress == null) {
            return BaseResult.fail(i18nService.getMessage("20061"));
        }
        if (byUserAddress.getUserAddress().equalsIgnoreCase(tokenUser.getUserAddress())) {
            return BaseResult.fail(i18nService.getMessage("20061"));
        }
        if (uUserBaseInfo.getIsBindReferee() == 0) {
            return BaseResult.fail(i18nService.getMessage("20063"));
        }
        UUserBaseInfo uUserBaseInfoReq = uUserBaseInfoService.getUUserBaseInfo(freeTransferReq.getToAddress().toLowerCase());
        if (uUserBaseInfoReq.getIsBindReferee() == 0) {
            return BaseResult.fail(i18nService.getMessage("20064"));
        }

        UUserReferee uUserRefereeToken = uUserRefereeService.lambdaQuery().eq(UUserReferee::getUserAddress, tokenUser.getUserAddress()).one();
        if (uUserRefereeToken == null) {
            return BaseResult.fail(i18nService.getMessage("20062"));
        }
        UUserReferee uUserRefereeReq = uUserRefereeService.lambdaQuery().eq(UUserReferee::getUserAddress, freeTransferReq.getToAddress().toLowerCase()).one();
        if (uUserRefereeReq == null) {
            return BaseResult.fail(i18nService.getMessage("20062"));
        }
        String tokenUserId = String.valueOf(uUserRefereeToken.getUserId());
        String reqUserId = String.valueOf(uUserRefereeReq.getUserId());
        boolean flagReferee = false;
        if (!StringUtils.isEmpty(uUserRefereeReq.getRefereeRelation())) {
            String[] split = uUserRefereeReq.getRefereeRelation().split(",");
            for (String str : split) {
                if (str.equalsIgnoreCase(tokenUserId)) {
                    flagReferee = true;
                    break;
                }
            }
        } else {
            flagReferee = true;
        }
        if (!StringUtils.isEmpty(uUserRefereeToken.getRefereeRelation()) && !flagReferee) {
            String[] split = uUserRefereeToken.getRefereeRelation().split(",");
            for (String str : split) {
                if (str.equalsIgnoreCase(reqUserId)) {
                    flagReferee = true;
                    break;
                }
            }
        } else {
            flagReferee = true;
        }
        if (!flagReferee) {
            return BaseResult.fail(i18nService.getMessage("20062"));
        }
        boolean flag = uUserBaseInfoService.updateFreeTicket(tokenUser.getUserAddress(), new BigDecimal(freeTransferReq.getFreeNum()), 202, byUserAddress.getUserAddress());
        if (!flag) {
            return BaseResult.fail(i18nService.getMessage("20060"));
        }
        uUserBaseInfoService.updateFreeTicket(byUserAddress.getUserAddress(), new BigDecimal(freeTransferReq.getFreeNum()), 106, tokenUser.getUserAddress());
        FreeTransfer freeTransfer = new FreeTransfer();
        freeTransfer.setFromId(tokenUser.getId());
        freeTransfer.setFromAddress(tokenUser.getUserAddress());
        freeTransfer.setToId(byUserAddress.getId());
        freeTransfer.setToAddress(byUserAddress.getUserAddress());
        freeTransfer.setFreeNum(new BigDecimal(freeTransferReq.getFreeNum()));
        freeTransfer.setUpdateTime(LocalDateTime.now());
        freeTransfer.setCreateTime(LocalDateTime.now());
        save(freeTransfer);
        return BaseResult.success();
    }

    @Override
    public PageRespVO<FreeTransferListResp> freeTransferList(PageReqVO pageReqVO) {
        UUser tokenUser = uUserService.getTokenUser();
        PageRespVO<FreeTransferListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<FreeTransfer> wrapper = new LambdaQueryWrapper<FreeTransfer>().eq(FreeTransfer::getFromAddress, tokenUser.getUserAddress()).or().eq(FreeTransfer::getToAddress, tokenUser.getUserAddress()).orderByDesc(FreeTransfer::getCreateTime);
        IPage<FreeTransfer> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<FreeTransferListResp> respList = new ArrayList<>();
        List<FreeTransfer> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (FreeTransfer record : records) {
                FreeTransferListResp resp = new FreeTransferListResp();
                resp.setTime(record.getCreateTime());
                resp.setFromAddress(record.getFromAddress());
                resp.setToAddress(record.getToAddress());
                resp.setFreeNum(record.getFreeNum());
                if (record.getFromAddress().equalsIgnoreCase(tokenUser.getUserAddress())) {
                    resp.setType(2);
                } else {
                    resp.setType(1);
                }
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }
}
