package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.OfficialWebsiteNewsMapper;
import com.nft.cn.entity.Community;
import com.nft.cn.entity.OfficialWebsiteNews;
import com.nft.cn.service.OfficialWebsiteNewsService;
import com.nft.cn.vo.req.OfficialWebsiteNewsInfoReq;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfficialWebsiteNewsServiceImpl extends ServiceImpl<OfficialWebsiteNewsMapper, OfficialWebsiteNews> implements OfficialWebsiteNewsService {

    @Override
    public OfficialWebsiteNewsResp officialWebsiteNewsList(PageReqVO pageReqVO) {
        OfficialWebsiteNewsResp resp = new OfficialWebsiteNewsResp();
        OfficialWebsiteNewsFirstResp firstResp = new OfficialWebsiteNewsFirstResp();
        OfficialWebsiteNews officialWebsiteNews = baseMapper.selectFirstNews();
        if (officialWebsiteNews != null) {
            firstResp.setId(officialWebsiteNews.getId());
            firstResp.setTitle(officialWebsiteNews.getTitleEn());
            firstResp.setContent(officialWebsiteNews.getContentEnText());
            firstResp.setReleaseTime(officialWebsiteNews.getReleaseTime());
        }
        resp.setOfficialWebsiteNewsFirstResp(firstResp);
        List<OfficialWebsiteAnnouncementListResp> announcementListRespList = new ArrayList<>();
        List<OfficialWebsiteNews> list = lambdaQuery().select(OfficialWebsiteNews::getId, OfficialWebsiteNews::getTitleEn, OfficialWebsiteNews::getContentEnText, OfficialWebsiteNews::getReleaseTime).eq(OfficialWebsiteNews::getType, 2).lt(OfficialWebsiteNews::getReleaseTime, LocalDateTime.now()).orderByDesc(OfficialWebsiteNews::getWeight, OfficialWebsiteNews::getReleaseTime).last("limit 3").list();
        if (!CollectionUtils.isEmpty(list)) {
            for (OfficialWebsiteNews websiteNews : list) {
                OfficialWebsiteAnnouncementListResp announcementListResp = new OfficialWebsiteAnnouncementListResp();
                announcementListResp.setId(websiteNews.getId());
                announcementListResp.setTitle(websiteNews.getTitleEn());
                announcementListResp.setContent(websiteNews.getContentEnText());
                announcementListResp.setReleaseTime(websiteNews.getReleaseTime());
                announcementListRespList.add(announcementListResp);
            }
        }
        resp.setOfficialWebsiteAnnouncementListRespList(announcementListRespList);
        PageRespVO<OfficialWebsiteNewsListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<OfficialWebsiteNews> wrapper = new LambdaQueryWrapper<OfficialWebsiteNews>().select(OfficialWebsiteNews::getId, OfficialWebsiteNews::getTitleEn, OfficialWebsiteNews::getContentEnText, OfficialWebsiteNews::getReleaseTime).eq(OfficialWebsiteNews::getType, 1);
        if (officialWebsiteNews != null) {
            wrapper.ne(OfficialWebsiteNews::getId, officialWebsiteNews.getId());
        }
        wrapper.lt(OfficialWebsiteNews::getReleaseTime, LocalDateTime.now()).orderByDesc(OfficialWebsiteNews::getWeight, OfficialWebsiteNews::getReleaseTime);
        IPage<OfficialWebsiteNews> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<OfficialWebsiteNewsListResp> newsRespList = new ArrayList<>();
        List<OfficialWebsiteNews> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (OfficialWebsiteNews record : records) {
                OfficialWebsiteNewsListResp newsResp = new OfficialWebsiteNewsListResp();
                newsResp.setId(record.getId());
                newsResp.setTitle(record.getTitleEn());
                newsResp.setContent(record.getContentEnText());
                newsResp.setReleaseTime(record.getReleaseTime());
                newsRespList.add(newsResp);
            }
        }
        respPageRespVO.setList(newsRespList);
        resp.setOfficialWebsiteNewsListRespList(respPageRespVO);
        return resp;
    }

    @Override
    public OfficialWebsiteNewsResp listHaveFirst(PageReqVO pageReqVO) {
        OfficialWebsiteNewsResp resp = new OfficialWebsiteNewsResp();
        PageRespVO<OfficialWebsiteNewsListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<OfficialWebsiteNews> wrapper = new LambdaQueryWrapper<OfficialWebsiteNews>().select(OfficialWebsiteNews::getId, OfficialWebsiteNews::getTitleEn, OfficialWebsiteNews::getContentEnText, OfficialWebsiteNews::getReleaseTime).eq(OfficialWebsiteNews::getType, 1);
        wrapper.lt(OfficialWebsiteNews::getReleaseTime, LocalDateTime.now()).orderByDesc(OfficialWebsiteNews::getWeight, OfficialWebsiteNews::getReleaseTime);
        IPage<OfficialWebsiteNews> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<OfficialWebsiteNewsListResp> newsRespList = new ArrayList<>();
        List<OfficialWebsiteNews> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (OfficialWebsiteNews record : records) {
                OfficialWebsiteNewsListResp newsResp = new OfficialWebsiteNewsListResp();
                newsResp.setId(record.getId());
                newsResp.setTitle(record.getTitleEn());
                newsResp.setContent(record.getContentEnText());
                newsResp.setReleaseTime(record.getReleaseTime());
                newsRespList.add(newsResp);
            }
        }
        respPageRespVO.setList(newsRespList);
        resp.setOfficialWebsiteNewsListRespList(respPageRespVO);
        return resp;
    }

    @Override
    public OfficialWebsiteNewsInfoResp officialWebsiteNewsInfo(OfficialWebsiteNewsInfoReq officialWebsiteNewsInfoReq) {
        OfficialWebsiteNewsInfoResp resp = new OfficialWebsiteNewsInfoResp();
        OfficialWebsiteNews byId = getById(officialWebsiteNewsInfoReq.getId());
        if (byId.getReleaseTime().compareTo(LocalDateTime.now()) < 0) {
            resp.setId(byId.getId());
            resp.setType(byId.getType());
            resp.setTitle(byId.getTitleEn());
            resp.setContent(byId.getContentEn());
            resp.setReleaseTime(byId.getReleaseTime());
        }
        return resp;
    }

    @Override
    public OfficialWebsiteNewsFirstResp firstImgList() {
        OfficialWebsiteNewsFirstResp firstResp = new OfficialWebsiteNewsFirstResp();
        OfficialWebsiteNews officialWebsiteNews = baseMapper.selectFirstNewsAndImg();
        if (officialWebsiteNews != null) {
            firstResp.setId(officialWebsiteNews.getId());
            firstResp.setTitle(officialWebsiteNews.getTitleEn());
            firstResp.setContent(officialWebsiteNews.getContentEnText());
            firstResp.setReleaseTime(officialWebsiteNews.getReleaseTime());
            if (!StringUtils.isEmpty(officialWebsiteNews.getContentEnImg())) {
                String[] split = officialWebsiteNews.getContentEnImg().split("\"><img src=\"");
                List<String> imgList = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    String replaceAll = split[i].replaceAll("<img src=\"", "");
                    replaceAll = replaceAll.replaceAll("\">", "");
                    replaceAll = replaceAll.replaceAll("\"", "");
                    imgList.add(replaceAll);
                }
                firstResp.setImgList(imgList);
            }
        }
        return firstResp;
    }
}
