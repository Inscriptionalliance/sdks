package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.MintRankNodeMapper;
import com.nft.cn.entity.Community;
import com.nft.cn.entity.MintRankCommunity;
import com.nft.cn.entity.MintRankNode;
import com.nft.cn.service.MintRankNodeService;
import com.nft.cn.service.SSystemStatisticsService;
import com.nft.cn.service.UUserService;
import com.nft.cn.vo.req.MintRankNodeReq;
import com.nft.cn.vo.req.MintRankUserReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintRankCommunityResp;
import com.nft.cn.vo.resp.MintRankNodeResp;
import com.nft.cn.vo.resp.MintRankUserResp;
import com.nft.cn.vo.resp.PageRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class MintRankNodeServiceImpl extends ServiceImpl<MintRankNodeMapper, MintRankNode> implements MintRankNodeService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private SSystemStatisticsService sSystemStatisticsService;

    @Override
    public PageRespVO<MintRankNodeResp> mintRankNode(PageReqVO pageReqVO) {
        String mintIncomePhase = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue();
        PageRespVO<MintRankNodeResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<MintRankNode> wrapper = new LambdaQueryWrapper<MintRankNode>().eq(MintRankNode::getPhase, mintIncomePhase).le(MintRankNode::getRank, 10).orderByAsc(MintRankNode::getRank);
        IPage<MintRankNode> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<MintRankNodeResp> respList = new ArrayList<>();
        List<MintRankNode> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (MintRankNode record : records) {
                MintRankNodeResp resp = new MintRankNodeResp();
                resp.setUserAddress(uUserService.maskAddress(record.getUserAddress()));
                resp.setMintNum(record.getMintNum());
                resp.setRank(record.getRank());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public PageRespVO<MintRankNodeResp> mintRankNodeSelectedRank(MintRankNodeReq mintRankNodeReq) {
        String mintIncomePhase = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue();
        PageRespVO<MintRankNodeResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<MintRankNode> wrapper = new LambdaQueryWrapper<MintRankNode>().eq(MintRankNode::getPhase, mintIncomePhase).orderByAsc(MintRankNode::getRank);
        if (!StringUtils.isEmpty(mintRankNodeReq.getUserAddress())) {
            wrapper.eq(MintRankNode::getUserAddress, mintRankNodeReq.getUserAddress());
        }
        IPage<MintRankNode> page = page(new Page<>(mintRankNodeReq.getPageNum(), mintRankNodeReq.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<MintRankNodeResp> respList = new ArrayList<>();
        List<MintRankNode> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (MintRankNode record : records) {
                MintRankNodeResp resp = new MintRankNodeResp();
                resp.setUserAddress(uUserService.maskAddress(record.getUserAddress()));
                resp.setMintNum(record.getMintNum());
                resp.setRank(record.getRank());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }


}
