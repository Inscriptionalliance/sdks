package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.UUserRefereeNumMapper;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserReferee;
import com.nft.cn.entity.UUserRefereeNum;
import com.nft.cn.service.SSystemConfigService;
import com.nft.cn.service.UUserRefereeNumService;
import com.nft.cn.service.UUserRefereeService;
import com.nft.cn.service.UUserService;
import com.nft.cn.vo.resp.InvalidRefereeNumListResp;
import com.nft.cn.vo.resp.RefereeNumUserListResp;
import com.nft.cn.vo.resp.TeamInfoResp;
import com.nft.cn.vo.resp.ValidRefereeNumListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UUserRefereeNumServiceImpl extends ServiceImpl<UUserRefereeNumMapper, UUserRefereeNum> implements UUserRefereeNumService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private UUserRefereeService uUserRefereeService;


    @Override
    public UUserRefereeNum getByRefereeNum(String refereeNum) {
        return lambdaQuery().eq(UUserRefereeNum::getRefereeNum, refereeNum).one();
    }


    @Override
    public List<ValidRefereeNumListResp> validRefereeNumList() {
        UUser tokenUser = uUserService.getTokenUser();
        List<UUserRefereeNum> list = lambdaQuery().eq(UUserRefereeNum::getUserAddress, tokenUser.getUserAddress()).eq(UUserRefereeNum::getStatus, 0).list();
        List<ValidRefereeNumListResp> respList = new ArrayList<>();
        for (UUserRefereeNum uUserRefereeNum : list) {
            ValidRefereeNumListResp resp = new ValidRefereeNumListResp();
            resp.setRefereeNum(uUserRefereeNum.getRefereeNum());
            respList.add(resp);
        }
        return respList;
    }

    @Override
    public List<InvalidRefereeNumListResp> invalidRefereeNumList() {
        UUser tokenUser = uUserService.getTokenUser();
        List<UUserRefereeNum> list = lambdaQuery().eq(UUserRefereeNum::getUserAddress, tokenUser.getUserAddress()).eq(UUserRefereeNum::getStatus, 1).list();
        List<InvalidRefereeNumListResp> respList = new ArrayList<>();
        for (UUserRefereeNum uUserRefereeNum : list) {
            InvalidRefereeNumListResp resp = new InvalidRefereeNumListResp();
            resp.setRefereeNum(uUserRefereeNum.getRefereeNum());
            respList.add(resp);
        }
        return respList;
    }

    @Override
    public List<RefereeNumUserListResp> refereeNumUserList() {
        UUser tokenUser = uUserService.getTokenUser();
        List<UUserReferee> uUserRefereeList = uUserRefereeService.selectChildList(tokenUser.getId());
        List<RefereeNumUserListResp> respList = new ArrayList<>();
        for (UUserReferee uUserReferee : uUserRefereeList) {
            RefereeNumUserListResp resp = new RefereeNumUserListResp();
            resp.setRefereeNum(uUserReferee.getRefereeNum());
            resp.setIncomeStatus(uUserReferee.getIncomeStatus());
            resp.setRefereeTime(uUserReferee.getCreateTime());
            resp.setRefereeAddress(uUserReferee.getUserAddress());
            respList.add(resp);
        }
        return respList;
    }

    @Override
    public void awardRefereeNum(UUser uUser) {
        long loginIncomeRefereeNum = Long.parseLong(sSystemConfigService.getByKey(SystemConfigConstant.login_income_referee_num).getConfigValue());
        for (long i = 0; i < loginIncomeRefereeNum; i++) {
            UUserRefereeNum uUserRefereeNum = new UUserRefereeNum();
            uUserRefereeNum.setUserId(uUser.getId());
            uUserRefereeNum.setUserAddress(uUser.getUserAddress());
            uUserRefereeNum.setCreateTime(LocalDateTime.now());
            uUserRefereeNum.setUpdateTime(LocalDateTime.now());
            String refereeNum = getRefereeNum();
            uUserRefereeNum.setRefereeNum(refereeNum);
            save(uUserRefereeNum);
        }
    }

    @Override
    public List<UUserRefereeNum> selectUser50() {
        return baseMapper.selectUser50();
    }

    private String getRefereeNum() {
        do {
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            String english = "abcdefghijklmnopqrstuvwxyz0123456789";
            for (int i = 0; i < 6; i++) {
                sb.append(english.charAt(random.nextInt(36)));
            }
            String number = sb.toString();
            UUserRefereeNum byRefereeNum = getByRefereeNum(number);
            if (byRefereeNum == null) {
                return number;
            }
        } while (true);
    }



}
