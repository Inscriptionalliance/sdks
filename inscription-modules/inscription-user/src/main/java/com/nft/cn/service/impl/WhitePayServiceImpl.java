package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.WhitePayMapper;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.WhitePay;
import com.nft.cn.service.UUserService;
import com.nft.cn.service.WhitePayService;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.WhitePayListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class WhitePayServiceImpl extends ServiceImpl<WhitePayMapper, WhitePay> implements WhitePayService {

    @Autowired
    private UUserService uUserService;


    @Override
    public PageRespVO<WhitePayListResp> whitePayList(PageReqVO pageReqVO) {
        UUser tokenUser = uUserService.getTokenUser();
        PageRespVO<WhitePayListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<WhitePay> wrapper = new LambdaQueryWrapper<WhitePay>().eq(WhitePay::getUserAddress, tokenUser.getUserAddress()).orderByDesc(WhitePay::getCreateTime);
        IPage<WhitePay> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<WhitePayListResp> respList = new ArrayList<>();
        List<WhitePay> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (WhitePay record : records) {
                WhitePayListResp resp = new WhitePayListResp();
                resp.setTime(record.getCreateTime());
                resp.setWhiteNum(record.getWhiteNum());
                resp.setAmount(record.getAmount());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }
}
