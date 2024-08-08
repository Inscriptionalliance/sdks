package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintRankMapper;
import com.nft.cn.dao.MintUserMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.MintRankService;
import com.nft.cn.service.MintUserService;
import com.nft.cn.service.UUserRefereeService;
import com.nft.cn.service.UUserService;
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
public class MintRankServiceImpl extends ServiceImpl<MintRankMapper, MintRank> implements MintRankService {

    @Autowired
    private UUserService userService;
    @Autowired
    private MintUserMapper mintUserMapper;
    @Autowired
    private UUserRefereeService uUserRefereeService;

    @Override
    public PageRespVO<MintRankResp> mintRank(PageReqVO pageReqVO) {
        PageRespVO<MintRankResp> respPageRespVO = new PageRespVO<>();
        Page<MintRankUser> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        page = mintUserMapper.selectMintRankPage(page, null);
        respPageRespVO.pageInit(page);
        List<MintRankResp> respList = new ArrayList<>();
        List<MintRankUser> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<String> userAddressList = records.stream().map(MintRankUser::getUserAddress).collect(Collectors.toList());
            List<UUserReferee> uUserRefereeList = uUserRefereeService.listNoRelation(userAddressList);
            for (int i = 0; i < records.size(); i++) {
                MintRankResp resp = new MintRankResp();
                resp.setUserAddress(userService.maskAddress(records.get(i).getUserAddress()));
                int finalI = i;
                List<UUserReferee> collect = uUserRefereeList.stream().filter(uUserReferee -> uUserReferee.getUserAddress().equalsIgnoreCase(records.get(finalI).getUserAddress())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(collect)) {
                    UUserReferee uUserReferee = collect.get(0);
                    if (!StringUtils.isEmpty(uUserReferee.getRefereeUserAddress())) {
                        resp.setRefereeAddress(userService.maskAddress(uUserReferee.getRefereeUserAddress()));
                    }
                }
                resp.setMintNum(records.get(i).getMintNum().divide(new BigDecimal("10000"), 0));
                long rank = (respPageRespVO.getCurrent() - 1) * respPageRespVO.getSize() + 1 + i;
                resp.setRank(rank);
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
