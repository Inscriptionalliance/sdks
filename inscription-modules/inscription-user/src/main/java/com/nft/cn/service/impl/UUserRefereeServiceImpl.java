package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.UUserRefereeMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.vo.req.PageReqVO;
import com.nft.cn.vo.resp.LastRefereeResp;
import com.nft.cn.vo.resp.PageRespVO;
import com.nft.cn.vo.resp.TeamRankResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UUserRefereeServiceImpl extends ServiceImpl<UUserRefereeMapper, UUserReferee> implements UUserRefereeService {

    @Autowired
    private UUserService userService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private UUserRefereeNumService uUserRefereeNumService;
    @Autowired
    private ZUserIpService zUserIpService;

    @Override
    public void saveRelationship(UUser newUser, String refereeUserAddress, String refereeNum) {
        String refereeRelation = null;
        Long refereeUserId = null;


        QueryWrapper<UUser> refereeUserQueryWrapper = new QueryWrapper<>();
        refereeUserQueryWrapper.eq("user_address", refereeUserAddress);

        UUser referenceUser = userService.getOne(refereeUserQueryWrapper);

        if (StringUtils.isEmpty(refereeUserAddress) || newUser.getUserAddress().equalsIgnoreCase(refereeUserAddress) || referenceUser == null) {
            return;
        }

        QueryWrapper<UUserReferee> refereeQueryWrapper = new QueryWrapper<>();
        refereeQueryWrapper.eq("user_address", refereeUserAddress);
        UUserReferee userReferee = baseMapper.selectOne(refereeQueryWrapper);

        if (userReferee != null ){
            refereeUserId = userReferee.getUserId();
            refereeRelation = userReferee.getRefereeRelation();
            if (StringUtils.isEmpty(refereeRelation)) {
                refereeRelation = userReferee.getUserId().toString();
            } else {
                refereeRelation = refereeRelation.concat(",").concat(userReferee.getUserId().toString());
            }
        }

        UUserReferee uUserReferee = new UUserReferee()
                .setUserId(newUser.getId())
                .setUserAddress(newUser.getUserAddress())
                .setRefereeUserId(refereeUserId)
                .setRefereeUserAddress(refereeUserAddress)
                .setRefereeNum(refereeNum)
                .setRefereeRelation(refereeRelation)
                .setCreateTime(LocalDateTime.now());
        int insertResult = baseMapper.insert(uUserReferee);
        if (insertResult > 0){
            newUser.setIsBind(1);
            userService.updateById(newUser);
            UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(newUser.getUserAddress());
            Integer currentPage = uUserBaseInfo.getCurrentPage();
            if (currentPage < 3) {
                currentPage = 3;
            }
            uUserBaseInfoService.lambdaUpdate()
                    .set(UUserBaseInfo::getIsBindReferee, 1)
                    .set(UUserBaseInfo::getCurrentPage, currentPage)
                    .eq(UUserBaseInfo::getUserAddress, newUser.getUserAddress())
                    .update();
            uUserRefereeNumService.lambdaUpdate()
                    .set(UUserRefereeNum::getRefereeAddress, newUser.getUserAddress())
                    .set(UUserRefereeNum::getRefereeTime, LocalDateTime.now())
                    .set(UUserRefereeNum::getUpdateTime, LocalDateTime.now())
                    .eq(UUserRefereeNum::getRefereeNum, refereeNum)
                    .update();
        }
        zUserIpService.saveUserIp(uUserReferee);
    }

    @Override
    public PageRespVO<LastRefereeResp> lastReferee(PageReqVO pageReqVO) {
        PageRespVO<LastRefereeResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<UUserReferee> wrapper = new LambdaQueryWrapper<UUserReferee>().orderByDesc(UUserReferee::getCreateTime);
        IPage<UUserReferee> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<LastRefereeResp> respList = new ArrayList<>();
        List<UUserReferee> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (UUserReferee record : records) {
                LastRefereeResp resp = new LastRefereeResp();
                UUser byId = userService.getById(record.getRefereeUserId());
                resp.setUserAddress(userService.maskAddress(record.getUserAddress()));
                resp.setRefereeAddress(userService.maskAddress(byId.getUserAddress()));
                resp.setRefereeTime(record.getCreateTime());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public List<String> getAllSubordinate(Long id) {
        return baseMapper.getAllSubordinate(id);
    }

    @Override
    public List<UUserReferee> getAllSubordinateList(Long id) {
        return baseMapper.getAllSubordinateList(id);
    }

    @Override
    public List<UUserReferee> selectChildList(Long id) {
        return baseMapper.selectChildListById(id);
    }

    @Override
    public UUserReferee selectByUserAddress(String userAddress) {
        return baseMapper.selectByUserAddress(userAddress);
    }

    @Override
    public List<UUserReferee> listNoRelation() {
        return baseMapper.listNoRelation();
    }

    @Override
    public List<UUserReferee> listNoRelation(List<String> userAddressList) {
        return baseMapper.listNoRelationByAddressList(userAddressList);
    }

    @Override
    public Long getAllSubordinateCount(Long id) {
        return baseMapper.getAllSubordinateCount(id);
    }

    @Override
    public Integer selectChildCount(Long id) {
        return baseMapper.selectChildCount(id);
    }

    @Override
    public List<Long> selectChildIdList(Long id) {
        return baseMapper.selectChildIdList(id);
    }

    @Override
    public List<UUserReferee> selectVipList(Long id) {
        return baseMapper.selectVipList(id);
    }


}
