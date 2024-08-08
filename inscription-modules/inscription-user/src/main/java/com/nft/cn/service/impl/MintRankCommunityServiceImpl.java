package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.MintRankCommunityMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.vo.req.MintRankCommunityReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MintRankCommunityServiceImpl extends ServiceImpl<MintRankCommunityMapper, MintRankCommunity> implements MintRankCommunityService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private SSystemStatisticsService sSystemStatisticsService;
    @Autowired
    private SSystemConfigService sSystemConfigService;

    @Override
    public PageRespVO<MintRankCommunityResp> mintRankCommunity(PageReqVO pageReqVO) {
        String mintIncomePhase = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue();
        BigDecimal selectedAchieve = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.mint_phase_community_mint_selected_num_ + mintIncomePhase).getConfigValue()).multiply(new BigDecimal("10000"));
        Page<MintRankCommunity> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        page = baseMapper.achieveAllListGtNumPage(page, selectedAchieve, Integer.parseInt(mintIncomePhase), null);
        PageRespVO<MintRankCommunityResp> respPageRespVO = new PageRespVO<>();
        respPageRespVO.pageInit(page);
        List<MintRankCommunityResp> respList = new ArrayList<>();
        List<MintRankCommunity> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (MintRankCommunity record : records) {
                MintRankCommunityResp resp = new MintRankCommunityResp();
                resp.setUserAddress(uUserService.maskAddress(record.getUserAddress()));
                Community one = communityService.lambdaQuery().eq(Community::getUserAddress, record.getUserAddress()).one();
                if (one != null) {
                    resp.setName(one.getName());
                }
                resp.setMintNum(record.getMintNum().divide(new BigDecimal("10000"), 0));
                resp.setRank(record.getRank());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public PageRespVO<MintRankCommunityResp> mintRankCommunitySelectedRank(MintRankCommunityReq mintRankCommunityReq) {
        String mintIncomePhase = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue();
        PageRespVO<MintRankCommunityResp> respPageRespVO = new PageRespVO<>();
        List<String> userAddressList = null;
        if (!StringUtils.isEmpty(mintRankCommunityReq.getName())) {
            List<Community> list = communityService.lambdaQuery().like(Community::getName, mintRankCommunityReq.getName()).list();
            userAddressList = list.stream().map(Community::getUserAddress).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userAddressList)) {
                respPageRespVO.setList(new ArrayList<>());
                return respPageRespVO;
            }
        }
        BigDecimal selectedAchieve = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.mint_phase_community_mint_selected_num_ + mintIncomePhase).getConfigValue()).multiply(new BigDecimal("10000"));
        Page<MintRankCommunity> page = new Page<>(mintRankCommunityReq.getPageNum(), mintRankCommunityReq.getPageSize());
        page = baseMapper.achieveAllListGtNumPage(page, selectedAchieve, Integer.parseInt(mintIncomePhase), userAddressList);
        respPageRespVO.pageInit(page);
        List<MintRankCommunityResp> respList = new ArrayList<>();
        List<MintRankCommunity> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (MintRankCommunity record : records) {
                MintRankCommunityResp resp = new MintRankCommunityResp();
                resp.setUserAddress(uUserService.maskAddress(record.getUserAddress()));
                Community one = communityService.lambdaQuery().eq(Community::getUserAddress, record.getUserAddress()).one();
                if (one != null) {
                    resp.setName(one.getName());
                }
                resp.setMintNum(record.getMintNum().divide(new BigDecimal("10000"), 0));
                resp.setRank(record.getRank());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public MintRankPhaseResp mintRankPhase() {
        String mintIncomePhase = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue();
        MintRankPhaseResp resp = new MintRankPhaseResp();
        resp.setMintIncomePhase(Integer.parseInt(mintIncomePhase));
        return resp;
    }
}
