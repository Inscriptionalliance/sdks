package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintUserHoldMapper;
import com.nft.cn.dao.TeamRankMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamRankServiceImpl extends ServiceImpl<TeamRankMapper, TeamRank> implements TeamRankService {

    @Autowired
    private UUserRefereeService uUserRefereeService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;

    @Override
    public void teamRank() {

    }
}
