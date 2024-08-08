package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.CommunityMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.CommunityLikeReq;
import com.nft.cn.vo.req.CommunityListReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.CommunityListResp;
import com.nft.cn.vo.resp.FreeTransferListResp;
import com.nft.cn.vo.resp.PageRespVO;
import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private CommunityLikeService communityLikeService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;


    @Override
    public PageRespVO<CommunityListResp> communityListPassToken(CommunityListReq communityListReq) {
        if (communityListReq.getArea() == null) {
            communityListReq.setArea(1);
        }
        PageRespVO<CommunityListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<Community>().eq(Community::getArea, communityListReq.getArea());
        if (!StringUtils.isEmpty(communityListReq.getNum())) {
            wrapper.like(Community::getNum, communityListReq.getNum());
        }
        wrapper.orderByAsc(Community::getOrderNum);
        IPage<Community> page = page(new Page<>(communityListReq.getPageNum(), communityListReq.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<CommunityListResp> respList = new ArrayList<>();
        List<Community> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (Community record : records) {
                CommunityListResp resp = new CommunityListResp();
                BeanUtils.copyProperties(record, resp);
                resp.setTodayLike(0);
                if (record.getImageStatus() != 1) {
                    resp.setLogoImage(null);
                }
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public PageRespVO<CommunityListResp> communityList(CommunityListReq communityListReq) {
        UUser tokenUser = uUserService.getTokenUser();
        LocalDate now = LocalDate.now();
        PageRespVO<CommunityListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<Community>().eq(Community::getArea, communityListReq.getArea());
        if (!StringUtils.isEmpty(communityListReq.getNum())) {
            wrapper.like(Community::getNum, communityListReq.getNum());
        }
        wrapper.orderByAsc(Community::getOrderNum);
        IPage<Community> page = page(new Page<>(communityListReq.getPageNum(), communityListReq.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<CommunityListResp> respList = new ArrayList<>();
        List<Community> records = page.getRecords();
        Integer count = communityLikeService.lambdaQuery().eq(CommunityLike::getUserAddress, tokenUser.getUserAddress()).groupBy(CommunityLike::getCommunityId).gt(CommunityLike::getCreateTime, now).count();
        if (!CollectionUtils.isEmpty(records)) {
            for (Community record : records) {
                CommunityListResp resp = new CommunityListResp();
                BeanUtils.copyProperties(record, resp);
                if (count != null && count > 0) {
                    resp.setTodayLike(1);
                } else {
                    resp.setTodayLike(0);
                }
                if (record.getImageStatus() != 1) {
                    resp.setLogoImage(null);
                }
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public BaseResult<String> communityLike(CommunityLikeReq communityLikeReq) {
        UUser tokenUser = uUserService.getTokenUser();
        if (communityLikeReq.getId() == null) {
            return BaseResult.fail(i18nService.getMessage("20071"));
        }
        Community byId = getById(communityLikeReq.getId());
        if (byId == null) {
            return BaseResult.fail(i18nService.getMessage("20071"));
        }
        LocalDate now = LocalDate.now();
        List<CommunityLike> list = communityLikeService.lambdaQuery().eq(CommunityLike::getUserAddress, tokenUser.getUserAddress()).ge(CommunityLike::getCreateTime, now).list();
        if (CollectionUtils.isEmpty(list)) {
            CommunityLike communityLike = new CommunityLike();
            communityLike.setUserId(tokenUser.getId());
            communityLike.setUserAddress(tokenUser.getUserAddress());
            communityLike.setCommunityId(communityLikeReq.getId());
            communityLike.setCreateTime(LocalDateTime.now());
            communityLike.setUpdateTime(LocalDateTime.now());
            communityLikeService.save(communityLike);
            updateLikeNum(communityLikeReq.getId());
            Integer count = communityLikeService.lambdaQuery().eq(CommunityLike::getUserAddress, tokenUser.getUserAddress()).eq(CommunityLike::getCommunityId, communityLikeReq.getId()).count();
            if (count == 1) {
                updatePartakeNum(communityLikeReq.getId());
            }
            BigDecimal communityLikeIncomeCredit = new BigDecimal(sSystemConfigService.getByKey(SystemConfigConstant.community_like_income_credit).getConfigValue());
            uUserBaseInfoService.updateCredit(tokenUser.getUserAddress(), communityLikeIncomeCredit, 109, null);
            if (isWeekLikeIncome(tokenUser.getUserAddress(), now)) {
                Long incomeRarityTicket = Long.parseLong(sSystemConfigService.getByKey(SystemConfigConstant.community_continuous_like_income_rarity_ticket).getConfigValue());
                uUserBaseInfoService.updateRarityTicket(tokenUser.getUserAddress(), incomeRarityTicket, 110, null);
            }
            return BaseResult.success();
        } else {
            return BaseResult.fail(i18nService.getMessage("20070"));
        }
    }

    @Override
    public void updateLikeNum(Long id) {
        while (true) {
            Community community = getById(id);
            if (community == null) {
                return;
            }
            Long likeNum = community.getLikeNum();
            Long likeNumNew = likeNum + 1;
            boolean update = lambdaUpdate()
                    .set(Community::getLikeNum, likeNumNew)
                    .set(Community::getUpdateTime, LocalDateTime.now())
                    .eq(Community::getLikeNum, likeNum)
                    .eq(Community::getId, community.getId())
                    .update();
            if (update) {
                break;
            }
        }
    }

    @Override
    public void updatePartakeNum(Long id) {
        while (true) {
            Community community = getById(id);
            if (community == null) {
                return;
            }
            Long partakeNum = community.getPartakeNum();
            Long partakeNumNew = partakeNum + 1;
            boolean update = lambdaUpdate()
                    .set(Community::getPartakeNum, partakeNumNew)
                    .set(Community::getUpdateTime, LocalDateTime.now())
                    .eq(Community::getPartakeNum, partakeNum)
                    .eq(Community::getId, community.getId())
                    .update();
            if (update) {
                break;
            }
        }
    }

    @Override
    public boolean isWeekLikeIncome (String userAddress, LocalDate now) {
        boolean isIncome = false;
        LocalDate firstDayOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        List<CommunityLike> weekList = communityLikeService.lambdaQuery().eq(CommunityLike::getUserAddress, userAddress).ge(CommunityLike::getCreateTime, firstDayOfWeek).list();
        if (!CollectionUtils.isEmpty(weekList) && weekList.size() >= 5) {
            int todayOfWeek = now.getDayOfWeek().getValue();
            if (todayOfWeek == 5) {
                isIncome = true;
            } else if (todayOfWeek == 6) {
                boolean isWeek1Like = false;
                for (CommunityLike like : weekList) {
                    int likeOfWeek = like.getCreateTime().getDayOfWeek().getValue();
                    if (likeOfWeek == 1) {
                        isWeek1Like = true;
                        break;
                    }
                }
                if (!isWeek1Like) {
                    isIncome = true;
                }
            } else if (todayOfWeek == 7) {
                boolean isWeek1Like = false;
                boolean isWeek2Like = false;
                for (CommunityLike like : weekList) {
                    int likeOfWeek = like.getCreateTime().getDayOfWeek().getValue();
                    if (likeOfWeek == 1) {
                        isWeek1Like = true;
                    } else if (likeOfWeek == 2) {
                        isWeek2Like = true;
                    }
                }
                if (!isWeek2Like) {
                    if (!isWeek1Like) {
                        isIncome = true;
                    } else {
                        if (weekList.size() == 6) {
                            isIncome = true;
                        }
                    }
                }
            }
        }
        return isIncome;
    }


}
