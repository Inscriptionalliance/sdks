package com.nft.cn.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UUserMapper;
import com.nft.cn.entity.*;
import com.nft.cn.exception.UnauthorizedException;
import com.nft.cn.form.LoginPasswordForm;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.RedisUtil;
import com.nft.cn.util.SimpleUtil;
import com.nft.cn.util.TokenUtil;
import com.nft.cn.vo.req.UserRefereeNumReq;
import com.nft.cn.vo.resp.UserInfoResp;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UUserServiceImpl extends ServiceImpl<UUserMapper, UUser> implements UUserService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private UUserService userService;

    @Autowired
    private UUserRefereeService uUserRefereeService;
    @Autowired
    private UUserRefereeNumService uUserRefereeNumService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private UUserTwitterTokenService uUserTwitterTokenService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MintCommunityTeamAchieveService mintCommunityTeamAchieveService;

    @Override
    public UUser getTokenUser() {
        try {
            Claims jsonObject = TokenUtil.getTokenUserInfo();

            if (null == jsonObject) {
                throw new UnauthorizedException(i18nService.getMessage("10002"));
            }
            UUser uUser = JSON.parseObject(jsonObject.getSubject(), UUser.class);

            Long uUserId = uUser.getId();
            UUser userInfo = userService.getById(uUserId);

            if (!Optional.ofNullable(userInfo).isPresent()){
                throw new UnauthorizedException(i18nService.getMessage("10002"));
            }
            return uUser;
        } catch (Exception e) {
            throw new UnauthorizedException(i18nService.getMessage("10002"));
        }
    }

    @Override
    public BaseResult<Map<String, Object>> loginByPass(LoginPasswordForm form) {
        if (StringUtils.isEmpty(form.getSign())) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }



        boolean validate = SimpleUtil.validate(form.getSign(), form.getMsg(),form.getUserAddress().toLowerCase());
        if (!validate){
            return BaseResult.fail(i18nService.getMessage("99999"));
        }

        Map<String, Object> map = new HashMap<>(3);
        try {
            String userAddress = form.getUserAddress().toLowerCase();

            QueryWrapper<UUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_address", userAddress);
            UUser user = baseMapper.selectOne(queryWrapper);

            if (user == null) {
                user = saveUser(form.getUserAddress(),form.getRefereeUserAddress());
            } else {
                UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(user.getUserAddress());
                uUserBaseInfoService.lambdaUpdate().set(UUserBaseInfo::getLastLoginTime, LocalDateTime.now()).eq(UUserBaseInfo::getId, uUserBaseInfo.getId()).update();
                userLoginService.login(user);
            }
            String token = tokenService.getToken(user.getId(),user);
            map.put("token", token);
            return BaseResult.success(map);

        } catch (Exception e) {
            throw new RuntimeException(i18nService.getMessage("10043"));
        }
    }

    @Override
    public BaseResult<String> userRefereeNum(UUser userByToken, UserRefereeNumReq userRefereeNumReq) {
        UUserRefereeNum uUserRefereeNum = uUserRefereeNumService.getByRefereeNum(userRefereeNumReq.getRefereeNum());
        if (uUserRefereeNum == null) {
            return BaseResult.fail(i18nService.getMessage("20011"));
        }
        if (uUserRefereeNum.getStatus() == 1) {
            return BaseResult.fail(i18nService.getMessage("20011"));
        }
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(userByToken.getUserAddress());
        if (uUserBaseInfo.getIsBindReferee() == 1) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        uUserRefereeService.saveRelationship(userByToken,uUserRefereeNum.getUserAddress(), userRefereeNumReq.getRefereeNum());
        return BaseResult.success("SUCCESS");
    }

    @Override
    public UUser getByUserAddress(String userAddress) {
        return lambdaQuery().eq(UUser::getUserAddress, userAddress).one();
    }

    @Override
    public UserInfoResp userInfo() {
        UUser tokenUser = getTokenUser();
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(tokenUser.getUserAddress());
        UserInfoResp resp = new UserInfoResp();
        BeanUtils.copyProperties(tokenUser, resp);
        BeanUtils.copyProperties(uUserBaseInfo, resp);
        UUserTwitterToken uUserTwitterToken = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserAddress, tokenUser.getUserAddress()).one();
        if (uUserTwitterToken != null) {
            resp.setIsBindTwitter(1);
        } else {
            resp.setIsBindTwitter(0);
        }
        return resp;
    }

    @Override
    public String maskAddress(String userAddress) {
        if (userAddress == null || userAddress.length() < 10) {
            return userAddress;
        }
        String afterAddress = userAddress.substring(0, 4);
        String lastAddress = userAddress.substring(userAddress.length() - 4);
        StringBuilder sb = new StringBuilder(afterAddress);
        sb.append("*").append("*").append("*").append(lastAddress);
        return sb.toString();
    }

    @Override
    public List<UUser> getChildUser(Long id) {
        return baseMapper.selectChildUser(id);
    }

    public UUser saveUser(String userAddress,String refereeUserAddress) {
        UUser newUser = new UUser()
                .setUpdateTime(LocalDateTime.now())
                .setCreateTime(LocalDateTime.now())
                .setUserAddress(userAddress.toLowerCase());
        baseMapper.insert(newUser);
        uUserBaseInfoService.getUUserBaseInfo(newUser.getUserAddress());
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(newUser.getUserAddress());
        Integer currentPage = uUserBaseInfo.getCurrentPage();
        if (currentPage < 2) {
            currentPage = 2;
        }
        uUserBaseInfoService.lambdaUpdate()
                .set(UUserBaseInfo::getIsBindOkx, 1)
                .set(UUserBaseInfo::getIsBindUnisat, 1)
                .set(UUserBaseInfo::getCurrentPage, currentPage)
                .eq(UUserBaseInfo::getUserAddress, newUser.getUserAddress())
                .update();
        MintCommunityTeamAchieve mintCommunityTeamAchieve = new MintCommunityTeamAchieve();
        mintCommunityTeamAchieve.setUserId(newUser.getId());
        mintCommunityTeamAchieve.setUserAddress(newUser.getUserAddress());
        mintCommunityTeamAchieveService.save(mintCommunityTeamAchieve);
        return newUser;
    }



}
