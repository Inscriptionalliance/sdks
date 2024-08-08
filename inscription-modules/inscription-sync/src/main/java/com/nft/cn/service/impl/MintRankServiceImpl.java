package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintRankMapper;
import com.nft.cn.dao.MintUserHoldMapper;
import com.nft.cn.dao.MintUserMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.MintRankService;
import com.nft.cn.service.MintUserHoldService;
import com.nft.cn.service.UUserRefereeService;
import com.nft.cn.service.UUserService;
import com.nft.cn.vo.resp.MintRankResp;
import com.nft.cn.vo.resp.PageRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MintRankServiceImpl extends ServiceImpl<MintRankMapper, MintRank> implements MintRankService {

    @Autowired
    private MintUserHoldMapper mintUserHoldMapper;
    @Autowired
    private UUserRefereeService uUserRefereeService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintUserMapper mintUserMapper;

    @Override
    public void mintRank() {
        List<MintUser> mintUserList = mintUserMapper.getMintRank();
        if (!CollectionUtils.isEmpty(mintUserList)) {
            List<UUserReferee> uUserRefereeList = uUserRefereeService.listNoRelation();
            for (int i = 0; i < mintUserList.size(); i++) {
                BigDecimal mintNum = mintUserList.get(i).getMintNum();
                long rank = i + 1;
                MintRank one = lambdaQuery().eq(MintRank::getUserAddress, mintUserList.get(i).getUserAddress()).one();
                if (one == null) {
                    UUserReferee uUserReferee = null;
                    int finalI = i;
                    List<UUserReferee> collect = uUserRefereeList.stream().filter(uUserReferee1 -> uUserReferee1.getUserAddress().equalsIgnoreCase(mintUserList.get(finalI).getUserAddress())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)) {
                        uUserReferee = collect.get(0);
                    }
                    one = new MintRank();
                    one.setUserId(mintUserList.get(i).getUserId());
                    one.setUserAddress(mintUserList.get(i).getUserAddress());
                    if (uUserReferee != null) {
                        one.setRefereeId(uUserReferee.getId());
                        one.setRefereeAddress(uUserReferee.getUserAddress());
                    }
                    one.setRank(rank);
                    one.setMintNum(mintNum);
                    one.setCreateTime(LocalDateTime.now());
                    one.setUpdateTime(LocalDateTime.now());
                    save(one);
                } else {
                    one.setRank(rank);
                    one.setMintNum(mintNum);
                    one.setUpdateTime(LocalDateTime.now());
                    updateById(one);
                }

            }
        }
    }
}
