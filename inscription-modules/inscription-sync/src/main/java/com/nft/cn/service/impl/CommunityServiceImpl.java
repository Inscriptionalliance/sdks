package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.CommunityMapper;
import com.nft.cn.entity.Community;
import com.nft.cn.entity.CommunityLike;
import com.nft.cn.entity.UUser;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.CommunityLikeReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.CommunityListResp;
import com.nft.cn.vo.resp.PageRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {


}
