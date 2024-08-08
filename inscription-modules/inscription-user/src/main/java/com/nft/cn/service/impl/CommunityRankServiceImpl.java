package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.CommunityRankMapper;
import com.nft.cn.entity.Community;
import com.nft.cn.entity.CommunityLike;
import com.nft.cn.entity.CommunityRank;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.CommunityRankService;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.CommunityListResp;
import com.nft.cn.vo.resp.CommunityRankResp;
import com.nft.cn.vo.resp.PageRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityRankServiceImpl extends ServiceImpl<CommunityRankMapper, CommunityRank> implements CommunityRankService {

    @Override
    public PageRespVO<CommunityRankResp> communityRank(PageReqVO pageReqVO) {
        PageRespVO<CommunityRankResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<CommunityRank> wrapper = new LambdaQueryWrapper<CommunityRank>().eq(CommunityRank::getValid, 1).orderByAsc(CommunityRank::getRank);
        IPage<CommunityRank> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<CommunityRankResp> respList = new ArrayList<>();
        List<CommunityRank> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (CommunityRank record : records) {
                CommunityRankResp resp = new CommunityRankResp();
                BeanUtils.copyProperties(record, resp);
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
