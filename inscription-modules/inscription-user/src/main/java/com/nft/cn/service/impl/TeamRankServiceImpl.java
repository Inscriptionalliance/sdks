package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.TeamRankMapper;
import com.nft.cn.entity.MintRank;
import com.nft.cn.entity.TeamRank;
import com.nft.cn.service.TeamRankService;
import com.nft.cn.service.UUserService;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.MintRankResp;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.TeamRankResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamRankServiceImpl extends ServiceImpl<TeamRankMapper, TeamRank> implements TeamRankService {

    @Autowired
    private UUserService userService;

    @Override
    public PageRespVO<TeamRankResp> teamRank(PageReqVO pageReqVO) {
        PageRespVO<TeamRankResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<TeamRank> wrapper = new LambdaQueryWrapper<TeamRank>().orderByAsc(TeamRank::getRank);
        IPage<TeamRank> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<TeamRankResp> respList = new ArrayList<>();
        List<TeamRank> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (TeamRank record : records) {
                TeamRankResp resp = new TeamRankResp();
                resp.setUserAddress(userService.maskAddress(record.getUserAddress()));
                resp.setRefereeAddress(userService.maskAddress(record.getRefereeAddress()));
                resp.setCreditNum(record.getCreditNum());
                resp.setRank(record.getRank());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        Long total = respPageRespVO.getTotal();
        if (total > 10000) {
            respPageRespVO.setTotal(10000L);
            respPageRespVO.setPages(respPageRespVO.getTotal() / respPageRespVO.getSize());
        }
        return respPageRespVO;
    }

}
