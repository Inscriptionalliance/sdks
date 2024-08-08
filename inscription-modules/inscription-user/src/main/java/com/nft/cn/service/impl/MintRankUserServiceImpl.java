package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.MintCommunityTeamAchieveMapper;
import com.nft.cn.dao.MintRankUserMapper;
import com.nft.cn.entity.MintRankCommunity;
import com.nft.cn.entity.MintRankNode;
import com.nft.cn.entity.MintRankUser;
import com.nft.cn.service.MintRankUserService;
import com.nft.cn.service.SSystemConfigService;
import com.nft.cn.service.SSystemStatisticsService;
import com.nft.cn.service.UUserService;
import com.nft.cn.vo.req.MintRankUserReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintRankNodeResp;
import com.nft.cn.vo.resp.MintRankUserResp;
import com.nft.cn.vo.resp.PageRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MintRankUserServiceImpl extends ServiceImpl<MintRankUserMapper, MintRankUser> implements MintRankUserService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private SSystemStatisticsService sSystemStatisticsService;
    @Autowired
    private SSystemConfigService sSystemConfigService;

    @Override
    public PageRespVO<MintRankUserResp> mintRankUser(PageReqVO pageReqVO) {
        String mintIncomePhase = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue();
        BigDecimal selectedAchieve = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.mint_phase_user_mint_selected_num_ + mintIncomePhase).getConfigValue()).multiply(new BigDecimal("10000"));
        PageRespVO<MintRankUserResp> respPageRespVO = new PageRespVO<>();
        Page<MintRankUser> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        page = baseMapper.selectRefereeAchieve(page, null, selectedAchieve, Integer.parseInt(mintIncomePhase));
        respPageRespVO.pageInit(page);
        List<MintRankUserResp> respList = new ArrayList<>();
        List<MintRankUser> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (MintRankUser record : records) {
                MintRankUserResp resp = new MintRankUserResp();
                resp.setUserAddress(uUserService.maskAddress(record.getUserAddress()));
                resp.setMintNum(record.getMintNum().divide(new BigDecimal("10000"), 0));
                resp.setRank(record.getRank());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public PageRespVO<MintRankUserResp> mintRankUserSelectedRank(MintRankUserReq mintRankUserReq) {
        String mintIncomePhase = sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue();
        BigDecimal selectedAchieve = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.mint_phase_user_mint_selected_num_ + mintIncomePhase).getConfigValue()).multiply(new BigDecimal("10000"));
        PageRespVO<MintRankUserResp> respPageRespVO = new PageRespVO<>();
        Page<MintRankUser> page = new Page<>(mintRankUserReq.getPageNum(), mintRankUserReq.getPageSize());
        page = baseMapper.selectRefereeAchieve(page, mintRankUserReq.getUserAddress(), selectedAchieve, Integer.parseInt(mintIncomePhase));
        respPageRespVO.pageInit(page);
        List<MintRankUserResp> respList = new ArrayList<>();
        List<MintRankUser> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (MintRankUser record : records) {
                MintRankUserResp resp = new MintRankUserResp();
                resp.setUserAddress(uUserService.maskAddress(record.getUserAddress()));
                resp.setMintNum(record.getMintNum().divide(new BigDecimal("10000"), 0));
                resp.setRank(record.getRank());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

}
