package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemStatisticsConstant;
import com.nft.cn.dao.MintUserMapper;
import com.nft.cn.dao.MintUserPaidMapper;
import com.nft.cn.dao.VipUserMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.vo.MintSyncVO;
import com.nft.cn.vo.resp.MintMinAreaResp;
import com.nft.cn.vo.resp.MintRefereeUserResp;
import com.nft.cn.vo.resp.VipUserInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VipUserServiceImpl extends ServiceImpl<VipUserMapper, VipUser> implements VipUserService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private UUserRefereeService uUserRefereeService;
    @Autowired
    private SSystemStatisticsService sSystemStatisticsService;
    @Autowired
    private MintCommunityTeamAchieveService mintCommunityTeamAchieveService;
    @Autowired
    private MintCommunityPaidAchieveService mintCommunityPaidAchieveService;
    @Autowired
    private MintUserPaidMapper mintUserPaidMapper;

    @Override
    public VipUser getVipUser(String userAddress){
        VipUser vipUser = lambdaQuery().eq(VipUser::getUserAddress, userAddress).eq(VipUser::getValid, 1).one();
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(userAddress);
        if (uUserBaseInfo.getIsVip() == 1 || uUserBaseInfo.getIsNodeVip() == 1 || uUserBaseInfo.getIsAdvancedVip() == 1 || uUserBaseInfo.getIsStandardVip() == 1) {
            if (vipUser == null) {
                vipUser = new VipUser();
                vipUser.setUserId(uUserBaseInfo.getUserId());
                vipUser.setUserAddress(uUserBaseInfo.getUserAddress());
                String num = getNum();
                vipUser.setNum(num);
                vipUser.setValid(1);
                vipUser.setCreateTime(LocalDateTime.now());
                vipUser.setUpdateTime(LocalDateTime.now());
                if (uUserBaseInfo.getIsAdvancedVip() == 1) {
                    vipUser.setType(3);
                } else if (uUserBaseInfo.getIsVip() == 1) {
                    vipUser.setType(1);
                } else if (uUserBaseInfo.getIsNodeVip() == 1) {
                    vipUser.setType(2);
                } else if (uUserBaseInfo.getIsStandardVip() == 1) {
                    vipUser.setType(4);
                }
                save(vipUser);
            } else {
                vipUser.setValid(1);
                if (uUserBaseInfo.getIsAdvancedVip() == 1) {
                    vipUser.setType(3);
                } else if (uUserBaseInfo.getIsVip() == 1) {
                    vipUser.setType(1);
                } else if (uUserBaseInfo.getIsNodeVip() == 1) {
                    vipUser.setType(2);
                } else if (uUserBaseInfo.getIsStandardVip() == 1) {
                    vipUser.setType(4);
                }
                vipUser.setUpdateTime(LocalDateTime.now());
                updateById(vipUser);
            }
        } else {
            if (vipUser != null) {
                lambdaUpdate().set(VipUser::getValid, 1).set(VipUser::getType, 0).set(VipUser::getUpdateTime, LocalDateTime.now()).eq(VipUser::getId, vipUser.getId()).update();
            }
        }
        return lambdaQuery().eq(VipUser::getUserAddress, userAddress).eq(VipUser::getValid, 1).one();
    }


    @Override
    public VipUserInfoResp info() {
        UUser tokenUser = uUserService.getTokenUser();
        VipUserInfoResp resp = new VipUserInfoResp();
        VipUser vipUser = getVipUser(tokenUser.getUserAddress());
        if (vipUser != null) {
            resp.setIsHave(1);
            resp.setUserAddress(tokenUser.getUserAddress());
            resp.setCreateTime(vipUser.getCreateTime());
            resp.setNum(vipUser.getNum());
            resp.setType(vipUser.getType());
        } else {
            resp.setIsHave(0);
            resp.setType(0);
        }
        List<Community> list = communityService.lambdaQuery().eq(Community::getUserAddress, tokenUser.getUserAddress()).list();
        if (!CollectionUtils.isEmpty(list)) {
            resp.setCommunityName(list.get(0).getNum() + "   " + list.get(0).getName());
        }

        MintCommunityTeamAchieve one = mintCommunityTeamAchieveService.lambdaQuery().eq(MintCommunityTeamAchieve::getUserId, tokenUser.getId()).one();
        resp.setTeamMint(one.getTeamMintNum().divide(new BigDecimal("10000"), 0));
        resp.setPaidMintNum(one.getTeamPaidMintNum().divide(new BigDecimal("10000"), 0));
        resp.setFreeMintNum(one.getTeamMintNum().subtract(one.getTeamPaidMintNum()).divide(new BigDecimal("10000"), 0));
        Long allSubordinateCount = uUserRefereeService.getAllSubordinateCount(tokenUser.getId());
        if (allSubordinateCount != null) {
            resp.setTeamNum(allSubordinateCount.intValue());
        } else {
            resp.setTeamNum(0);
        }


        int phase = Integer.parseInt(sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue());
        BigDecimal allAchieve = BigDecimal.ZERO;
        BigDecimal bigArea = BigDecimal.ZERO;
        Long bigId = null;
        Integer refereeAddressNum = uUserRefereeService.selectChildCount(tokenUser.getId());
        if (refereeAddressNum == null) {
            resp.setRefereeAddressNum(0);
        } else {
            resp.setRefereeAddressNum(refereeAddressNum);
        }
        BigDecimal refereeMintNum = BigDecimal.ZERO;
        List<MintCommunityPaidAchieve> mintCommunityPaidAchieveList = mintCommunityPaidAchieveService.selectChild(tokenUser.getId());
        if (!CollectionUtils.isEmpty(mintCommunityPaidAchieveList)) {
            for (MintCommunityPaidAchieve mintCommunityPaidAchieve : mintCommunityPaidAchieveList) {
                BigDecimal areaAchieve = BigDecimal.ZERO;
                if (phase == 1) {
                    areaAchieve = mintCommunityPaidAchieve.getPaidMintNum1().add(mintCommunityPaidAchieve.getTeamPaidMintNum1());
                    refereeMintNum = refereeMintNum.add(mintCommunityPaidAchieve.getPaidMintNum1());
                } else if (phase == 2) {
                    areaAchieve = mintCommunityPaidAchieve.getPaidMintNum2().add(mintCommunityPaidAchieve.getTeamPaidMintNum2());
                    refereeMintNum = refereeMintNum.add(mintCommunityPaidAchieve.getPaidMintNum2());
                } else if (phase == 3) {
                    areaAchieve = mintCommunityPaidAchieve.getPaidMintNum3().add(mintCommunityPaidAchieve.getTeamPaidMintNum3());
                    refereeMintNum = refereeMintNum.add(mintCommunityPaidAchieve.getPaidMintNum3());
                } else if (phase == 4) {
                    areaAchieve = mintCommunityPaidAchieve.getPaidMintNum4().add(mintCommunityPaidAchieve.getTeamPaidMintNum4());
                    refereeMintNum = refereeMintNum.add(mintCommunityPaidAchieve.getPaidMintNum4());
                }
                if (areaAchieve.compareTo(bigArea) > 0) {
                    bigArea = areaAchieve;
                    bigId = mintCommunityPaidAchieve.getUserId();
                }
            }
        }
        resp.setRefereeMintNum(refereeMintNum.divide(new BigDecimal("10000"), 0));
        MintCommunityPaidAchieve mintCommunityPaidAchieve = mintCommunityPaidAchieveService.lambdaQuery().eq(MintCommunityPaidAchieve::getUserId, tokenUser.getId()).one();
        if (mintCommunityPaidAchieve != null) {
            if (phase == 1) {
                allAchieve = mintCommunityPaidAchieve.getTeamPaidMintNum1().subtract(bigArea);
            } else if (phase == 2) {
                allAchieve = mintCommunityPaidAchieve.getTeamPaidMintNum2().subtract(bigArea);
            } else if (phase == 3) {
                allAchieve = mintCommunityPaidAchieve.getTeamPaidMintNum3().subtract(bigArea);
            } else if (phase == 4) {
                allAchieve = mintCommunityPaidAchieve.getTeamPaidMintNum4().subtract(bigArea);
            }
        }

        LocalDate now = LocalDate.now().minusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = now.format(dateTimeFormatter);
        BigDecimal communityMintNumYesterday = BigDecimal.ZERO;
        if (bigId != null) {
            communityMintNumYesterday = mintUserPaidMapper.selectYesterdayCommunity(tokenUser.getId(), bigId, phase, format);
            if (communityMintNumYesterday == null) {
                communityMintNumYesterday = BigDecimal.ZERO;
            }
        }
        BigDecimal refereeMintNumYesterday = mintUserPaidMapper.selectYesterdayReferee(tokenUser.getId(), phase, format);
        if (refereeMintNumYesterday == null) {
            refereeMintNumYesterday = BigDecimal.ZERO;
        }

        resp.setCommunityMintNumYesterday(communityMintNumYesterday.divide(new BigDecimal("10000"), 0));
        resp.setRefereeMintNumYesterday(refereeMintNumYesterday.divide(new BigDecimal("10000"), 0));

        Integer communityPartAddress = mintCommunityPaidAchieveService.getAllSubordinateCount(tokenUser.getId(), phase);
        if (communityPartAddress == null) {
            communityPartAddress = 0;
        }
        resp.setCommunityPartAddress(communityPartAddress);
        resp.setCommunityMintNum(allAchieve.divide(new BigDecimal("10000"), 0));
        resp.setBigAreaMintNum(bigArea.divide(new BigDecimal("10000"), 0));
        return resp;
    }


    @Override
    public VipUser getByNum(String num) {
        return lambdaQuery().eq(VipUser::getNum, num).one();
    }

    @Override
    public VipUserInfoResp communityList() {
        UUser tokenUser = uUserService.getTokenUser();
        VipUserInfoResp resp = new VipUserInfoResp();
        List<MintMinAreaResp> minAreaList = new ArrayList<>();
        BigDecimal bigArea = BigDecimal.ZERO;
        String bigAddress = null;
        Long bigId = null;
        BigDecimal allAchieve = BigDecimal.ZERO;
        List<Long> userIdList = new ArrayList<>();

        int phase = Integer.parseInt(sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue());
        List<MintCommunityPaidAchieve> mintCommunityPaidAchieveList = mintCommunityPaidAchieveService.selectChild(tokenUser.getId());
        if (!CollectionUtils.isEmpty(mintCommunityPaidAchieveList)) {
            for (MintCommunityPaidAchieve mintCommunityPaidAchieve : mintCommunityPaidAchieveList) {
                BigDecimal areaAchieve = BigDecimal.ZERO;
                if (phase == 1) {
                    areaAchieve = mintCommunityPaidAchieve.getPaidMintNum1().add(mintCommunityPaidAchieve.getTeamPaidMintNum1());
                } else if (phase == 2) {
                    areaAchieve = mintCommunityPaidAchieve.getPaidMintNum2().add(mintCommunityPaidAchieve.getTeamPaidMintNum2());
                } else if (phase == 3) {
                    areaAchieve = mintCommunityPaidAchieve.getPaidMintNum3().add(mintCommunityPaidAchieve.getTeamPaidMintNum3());
                } else if (phase == 4) {
                    areaAchieve = mintCommunityPaidAchieve.getPaidMintNum4().add(mintCommunityPaidAchieve.getTeamPaidMintNum4());
                }
                if (areaAchieve.compareTo(bigArea) > 0) {
                    if (bigArea.compareTo(BigDecimal.ZERO) > 0) {
                        MintMinAreaResp mintMinAreaResp = new MintMinAreaResp();
                        mintMinAreaResp.setId(mintCommunityPaidAchieve.getId());
                        mintMinAreaResp.setMintNum(bigArea.divide(new BigDecimal("10000"), 0));
                        mintMinAreaResp.setUserAddress(bigAddress);
                        mintMinAreaResp.setUserId(bigId);
                        minAreaList.add(mintMinAreaResp);
                        userIdList.add(bigId);
                    }
                    bigArea = areaAchieve;
                    bigId = mintCommunityPaidAchieve.getUserId();
                    bigAddress = mintCommunityPaidAchieve.getUserAddress();
                } else {
                    if (areaAchieve.compareTo(BigDecimal.ZERO) > 0) {
                        MintMinAreaResp mintMinAreaResp = new MintMinAreaResp();
                        mintMinAreaResp.setId(mintCommunityPaidAchieve.getId());
                        mintMinAreaResp.setMintNum(areaAchieve.divide(new BigDecimal("10000"), 0));
                        mintMinAreaResp.setUserAddress(mintCommunityPaidAchieve.getUserAddress());
                        mintMinAreaResp.setUserId(mintCommunityPaidAchieve.getUserId());
                        minAreaList.add(mintMinAreaResp);
                        userIdList.add(mintCommunityPaidAchieve.getUserId());
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(userIdList)) {
            LocalDate now = LocalDate.now().minusDays(1);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = now.format(dateTimeFormatter);
            List<MintUserPaid> mintUserPaidList = mintUserPaidMapper.selectYesterdayCommunityList(userIdList, phase, format);
            for (MintMinAreaResp mintMinAreaResp : minAreaList) {
                List<MintUserPaid> mintUserPaidListId = mintUserPaidList.stream().filter(mintUserPaid -> mintUserPaid.getUserId().equals(mintMinAreaResp.getUserId())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(mintUserPaidListId)) {
                    mintMinAreaResp.setMintNumYesterday(mintUserPaidListId.get(0).getMintNum().divide(new BigDecimal("10000"), 0));
                } else {
                    mintMinAreaResp.setMintNumYesterday(BigDecimal.ZERO);
                }
                mintMinAreaResp.setUserId(null);
            }
        }
        minAreaList = minAreaList.stream().sorted(Comparator.comparing(MintMinAreaResp::getMintNum, Comparator.reverseOrder())).collect(Collectors.toList());

        resp.setBigAreaMintNum(bigArea.divide(new BigDecimal("10000"), 0));
        resp.setMinMintAreaList(minAreaList);
        return resp;
    }

    @Override
    public VipUserInfoResp refereeList() {
        UUser tokenUser = uUserService.getTokenUser();
        VipUserInfoResp resp = new VipUserInfoResp();
        List<MintRefereeUserResp> refereeUserRespList = new ArrayList<>();

        int phase = Integer.parseInt(sSystemStatisticsService.getByKey(SystemStatisticsConstant.mint_mint_income_phase).getStatisticsValue());
        List<Long> userIdList = new ArrayList<>();

        List<UUserReferee> uUserRefereeList = uUserRefereeService.selectChildList(tokenUser.getId());
        List<MintCommunityPaidAchieve> mintCommunityPaidAchieveList = mintCommunityPaidAchieveService.selectChild(tokenUser.getId());
        for (MintCommunityPaidAchieve mintCommunityPaidAchieve : mintCommunityPaidAchieveList) {
            BigDecimal paidMintNum = BigDecimal.ZERO;
            if (phase == 1) {
                paidMintNum = mintCommunityPaidAchieve.getPaidMintNum1();
            } else if (phase == 2) {
                paidMintNum = mintCommunityPaidAchieve.getPaidMintNum2();
            } else if (phase == 3) {
                paidMintNum = mintCommunityPaidAchieve.getPaidMintNum3();
            } else if (phase == 4) {
                paidMintNum = mintCommunityPaidAchieve.getPaidMintNum4();
            }
            if (paidMintNum.compareTo(BigDecimal.ZERO) > 0) {
                List<UUserReferee> collect = uUserRefereeList.stream().filter(uUserReferee -> uUserReferee.getUserId().equals(mintCommunityPaidAchieve.getUserId())).collect(Collectors.toList());
                MintRefereeUserResp mintRefereeUserResp = new MintRefereeUserResp();
                mintRefereeUserResp.setId(mintCommunityPaidAchieve.getId());
                mintRefereeUserResp.setCreateTime(mintCommunityPaidAchieve.getCreateTime());
                if (!CollectionUtils.isEmpty(collect)) {
                    mintRefereeUserResp.setCreateTime(collect.get(0).getCreateTime());
                }
                mintRefereeUserResp.setUserAddress(mintCommunityPaidAchieve.getUserAddress());
                mintRefereeUserResp.setMintNum(paidMintNum.divide(new BigDecimal("10000"), 0));
                refereeUserRespList.add(mintRefereeUserResp);
                userIdList.add(mintCommunityPaidAchieve.getUserId());
            }
        }

        if (!CollectionUtils.isEmpty(userIdList)) {
            LocalDate now = LocalDate.now().minusDays(1);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = now.format(dateTimeFormatter);
            List<MintUserPaid> mintUserPaidList = mintUserPaidMapper.selectYesterdayRefereeList(userIdList, phase, format);
            for (MintRefereeUserResp mintRefereeUserResp : refereeUserRespList) {
                List<MintUserPaid> mintUserPaidListAddress = mintUserPaidList.stream().filter(mintUserPaid -> mintUserPaid.getUserAddress().equalsIgnoreCase(mintRefereeUserResp.getUserAddress())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(mintUserPaidListAddress)) {
                    mintRefereeUserResp.setMintNumYesterday(mintUserPaidListAddress.get(0).getMintNum().divide(new BigDecimal("10000"), 0));
                } else {
                    mintRefereeUserResp.setMintNumYesterday(BigDecimal.ZERO);
                }
            }
        }

        refereeUserRespList = refereeUserRespList.stream().sorted(Comparator.comparing(MintRefereeUserResp::getMintNum, Comparator.reverseOrder())).collect(Collectors.toList());
        resp.setRefereeUserRespList(refereeUserRespList);
        return resp;
    }

    private String getNum() {
        do {
            StringBuilder sb = new StringBuilder();
            sb.append("#");
            Random random = new Random();
            String english = "0123456789";
            for (int i = 0; i < 6; i++) {
                sb.append(english.charAt(random.nextInt(10)));
            }
            String number = sb.toString();
            VipUser vipUser = getByNum(number);
            if (vipUser == null) {
                return number;
            }
        } while (true);
    }


}
