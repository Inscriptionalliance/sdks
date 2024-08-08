package com.nft.cn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.RedisConstant;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.MintUserMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.*;
import com.nft.cn.vo.req.*;
import com.nft.cn.vo.resp.*;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MintUserServiceImpl extends ServiceImpl<MintUserMapper, MintUser> implements MintUserService {

    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private MintUserHoldService mintUserHoldService;
    @Autowired
    private MintDeployService mintDeployService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private NonceUserMintService nonceUserMintService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MintUserBoxService mintUserBoxService;
    @Autowired
    private UUserAuthNumService uUserAuthNumService;
    @Autowired
    private MintUserMapper mintUserMapper;

    @Value("${rpcUrl}")
    private String rpcUrl;


    @Value("${chainId}")
    private Integer chainId;
    @Value("${priKey}")
    private String priKey;
    @Value("${mintContractAddress}")
    private String mintContractAddress;

    @Override
    public BaseResult<MintMintResp> mint() {
        UUser tokenUser = uUserService.getTokenUser();
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        MintUser mintUser = null;
        String mintOne = "data:,{\"p\":\"denim-20\",\"op\":\"mint\",\"tick\":\"btia\",\"amt\":\"" + mintDeploy.getMintOne().intValue() + "\"}";
        String mintJsonHexOne = Hex.stringToHex(mintOne);
        BigDecimal mintTicket = mintUser.getMintTicket();
        StringBuilder sb = new StringBuilder();
        String mintJsonHex = sb.toString();
        Integer count = lambdaQuery().eq(MintUser::getUserAddress, tokenUser.getUserAddress()).eq(MintUser::getStatus, 4).count();
        MintMintResp resp = new MintMintResp();
        resp.setMintJsonHex(mintJsonHex);
        resp.setToAddress(tokenUser.getUserAddress());
        if (count > 0) {
            resp.setIsFirst(0);
        } else {
            resp.setIsFirst(1);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", mintUser.getId());
        UUserAuthNum uUserAuthNum = uUserAuthNumService.refreshAuthNum(tokenUser, 2, jsonObject.toJSONString());
        resp.setAuthNum(uUserAuthNum.getMintAuthNum());
        return BaseResult.success(resp);
    }


    @Override
    public BaseResult<MintPayBoxResp> payBox(MintMintReq mintMintReq) {
        UUser tokenUser = uUserService.getTokenUser();
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        MintUserHold mintUserHold = mintUserHoldService.getMintUserHold(tokenUser.getUserAddress(), mintDeploy.getId());
        BigDecimal mintCount = BigDecimal.ZERO;
        if (mintMintReq.getMintNum() != null && mintMintReq.getMintNum().compareTo(BigInteger.ZERO) > 0) {
            mintCount = new BigDecimal(mintMintReq.getMintNum());
        }
        BigDecimal useMintNum = BigDecimal.ZERO;
        BigDecimal useMintTicket = BigDecimal.ZERO;
        BigDecimal useFreeTicket = BigDecimal.ZERO;
        BigDecimal useAmount = BigDecimal.ZERO;
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(tokenUser.getUserAddress());
        if (mintCount.compareTo(BigDecimal.ZERO) <= 0) {
            return BaseResult.fail(i18nService.getMessage("20083"));
        }
        if ((uUserBaseInfo.getMintTicket().add(useMintTicket)).compareTo(mintCount) < 0) {
            return BaseResult.fail(i18nService.getMessage("20080"));
        }
        BigDecimal freeTicket = BigDecimal.ZERO;
        MintUser mintUser = saveMintUser(tokenUser, mintDeploy, mintCount, freeTicket);
        if (mintUser == null) {
            return BaseResult.fail(i18nService.getMessage("20087"));
        }
        mintUser.setMintNum(mintUser.getMintNum().subtract(useMintNum));
        mintUser.setAmount(mintUser.getAmount().subtract(useAmount));
        mintUser.setMintTicket(mintUser.getMintTicket().subtract(useMintTicket));
        mintUser.setFreeTicket(mintUser.getFreeTicket().subtract(useFreeTicket));
        MintUser mintUser2 = lambdaQuery().eq(MintUser::getUserAddress, tokenUser.getUserAddress()).eq(MintUser::getStatus, -2).one();
        mintUser.setCreateTime(LocalDateTime.now());
        mintUser.setUpdateTime(LocalDateTime.now());
        mintUser.setStatus(-2);
        if (mintUser2 != null) {
            mintUser.setId(mintUser2.getId());
            updateById(mintUser);
        } else {
            if (!save(mintUser)) {
                return BaseResult.fail(i18nService.getMessage("20084"));
            }
        }

        String enStr = getEnStr(mintUser);
        MintPayBoxResp resp = new MintPayBoxResp();
        resp.setSign(enStr);
        resp.setAmount(mintUser.getAmount());
        resp.setIsToContract(1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", mintUser.getId());
        String mintPayDeadlineSecond = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_deadline_second).getConfigValue();
        UUserAuthNum uUserAuthNum = uUserAuthNumService.refreshAuthNum(tokenUser, Long.parseLong(mintPayDeadlineSecond), 1, JSONObject.toJSONString(jsonObject));
        resp.setAuthNum(uUserAuthNum.getPayBoxAuthNum());
        return BaseResult.success(resp);
    }

    @Override
    public BaseResult<String> mintAuth(MintMintAuthReq mintMintAuthReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUserAuthNum authNum = uUserAuthNumService.getAuthNum(mintMintAuthReq.getAuthNum(), 2);
        if (authNum == null) {
            return BaseResult.fail(i18nService.getMessage("20215"));
        }
        if (authNum.getMintAuthNumStatus() != 1) {
            if (authNum.getMintAuthNumStatus() == 2) {
                return BaseResult.fail(i18nService.getMessage("20301"));
            } else if (authNum.getMintAuthNumStatus() == 3) {
                return BaseResult.fail(i18nService.getMessage("20305"));
            } else if (authNum.getMintAuthNumStatus() == 4) {
                return BaseResult.fail(i18nService.getMessage("20215"));
            } else {
                return BaseResult.fail(i18nService.getMessage("99999"));
            }
        }
        MintUser mintUser = null;
        try {
            mintUser = JSONObject.parseObject(authNum.getMintAuthNumJson(), MintUser.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        if (mintUser == null) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        mintUserHoldService.lambdaUpdate()
                .set(MintUserHold::getMintStatus, 3)
                .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                .eq(MintUserHold::getMintStatus, 2)
                .eq(MintUserHold::getUserAddress, tokenUser.getUserAddress())
                .update();
        lambdaUpdate()
                .set(MintUser::getStatus, 3)
                .set(MintUser::getUpdateTime, LocalDateTime.now())
                .eq(MintUser::getStatus, 2)
                .eq(MintUser::getId, mintUser.getId())
                .update();
        uUserAuthNumService.updateAuthNumStatus(tokenUser, 2, 2);
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> payBoxAuth(MintPayAuthReq mintPayAuthReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUserAuthNum authNum = uUserAuthNumService.getAuthNum(mintPayAuthReq.getAuthNum(), 1);
        if (authNum == null) {
            return BaseResult.fail(i18nService.getMessage("20215"));
        }
        if (authNum.getPayBoxAuthNumStatus() != 1) {
            if (authNum.getPayBoxAuthNumStatus() == 2) {
                return BaseResult.fail(i18nService.getMessage("20301"));
            } else if (authNum.getPayBoxAuthNumStatus() == 3) {
                return BaseResult.fail(i18nService.getMessage("20305"));
            } else if (authNum.getPayBoxAuthNumStatus() == 4) {
                return BaseResult.fail(i18nService.getMessage("20215"));
            } else {
                return BaseResult.fail(i18nService.getMessage("99999"));
            }
        }
        MintUser mintUser = null;
        try {
            mintUser = JSONObject.parseObject(authNum.getPayBoxAuthNumJson(), MintUser.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("20218"));
        }
        if (mintUser == null) {
            return BaseResult.fail(i18nService.getMessage("20218"));
        }
        boolean update = lambdaUpdate()
                .set(MintUser::getStatus, 1)
                .set(MintUser::getUpdateTime, LocalDateTime.now())
                .eq(MintUser::getStatus, -2)
                .eq(MintUser::getId, mintUser.getId())
                .update();
        if (update) {
            mintUserHoldService.lambdaUpdate()
                    .set(MintUserHold::getMintStatus, 1)
                    .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                    .eq(MintUserHold::getMintStatus, 0)
                    .eq(MintUserHold::getUserAddress, tokenUser.getUserAddress())
                    .update();
        }
        uUserAuthNumService.updateAuthNumStatus(tokenUser, 2, 1);
        return BaseResult.success();
    }


    @Override
    public BaseResult<String> mintAuthError(MintMintAuthErrorReq mintMintAuthErrorReq) {
        UUser tokenUser = uUserService.getTokenUser();
        UUserAuthNum uUserAuthNum = null;
        long currentTimeMillis = System.currentTimeMillis();
        do {
            uUserAuthNum = uUserAuthNumService.getAuthNum(mintMintAuthErrorReq.getAuthNum(), 2);
            if (uUserAuthNum == null) {
                if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                    return BaseResult.fail(i18nService.getMessage("20215"));
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
            }
            if (uUserAuthNum.getPayBoxAuthNumStatus() != 2) {
                if (uUserAuthNum.getPayBoxAuthNumStatus() == 1) {
                    if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                        return BaseResult.fail(i18nService.getMessage("20300"));
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
            } else {
                break;
            }
        } while (true);

        MintUser mintUser = null;
        try {
            mintUser = JSONObject.parseObject(uUserAuthNum.getMintAuthNumJson(), MintUser.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        if (mintUser == null) {
            return BaseResult.fail(i18nService.getMessage("99999"));
        }
        MintUser byId = getById(mintUser.getId());
        if (byId.getStatus() == 2 || byId.getStatus() == 3) {
            boolean update = lambdaUpdate()
                    .set(MintUser::getStatus, 2)
                    .set(MintUser::getUpdateTime, LocalDateTime.now())
                    .eq(MintUser::getStatus, 3)
                    .eq(MintUser::getId, mintUser.getId())
                    .update();
            if (update) {
                mintUserHoldService.lambdaUpdate()
                        .set(MintUserHold::getMintStatus, 2)
                        .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                        .eq(MintUserHold::getUserAddress, tokenUser.getUserAddress())
                        .eq(MintUserHold::getMintStatus, 3)
                        .update();
            }
        }
        uUserAuthNumService.updateAuthNumStatus(tokenUser, 3, 2);
        return BaseResult.success();
    }

    @Override
    public BaseResult<String> payBoxAuthError(MintPayAuthErrorReq mintPayAuthErrorReq) {
        UUser tokenUser = uUserService.getTokenUser();
        long currentTimeMillis = System.currentTimeMillis();
        UUserAuthNum authNum = null;
        do {
            authNum = uUserAuthNumService.getAuthNum(mintPayAuthErrorReq.getAuthNum(), 1);
            if (authNum == null) {
                if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                    return BaseResult.fail(i18nService.getMessage("20215"));
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
            }
            if (authNum.getPayBoxAuthNumStatus() != 2) {
                if (authNum.getPayBoxAuthNumStatus() == 1) {
                    if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                        return BaseResult.fail(i18nService.getMessage("20300"));
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
            } else {
                break;
            }
        } while (true);
        MintUser mintUser = null;
        try {
            mintUser = JSONObject.parseObject(authNum.getPayBoxAuthNumJson(), MintUser.class);
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("20218"));
        }
        if (mintUser == null) {
            return BaseResult.fail(i18nService.getMessage("20218"));
        }
        Integer count = lambdaQuery().eq(MintUser::getUserAddress, tokenUser.getUserAddress()).eq(MintUser::getStatus, -1).count();
        int mintStatus = 0;
        if (count != null && count > 0) {
            mintStatus = -1;
        }
        MintUser byId = getById(mintUser.getId());
        if (byId.getStatus() == 1 || byId.getStatus() == -2) {
            boolean update = lambdaUpdate()
                    .set(MintUser::getStatus, -2)
                    .set(MintUser::getUpdateTime, LocalDateTime.now())
                    .eq(MintUser::getStatus, 1)
                    .eq(MintUser::getId, mintUser.getId())
                    .update();
            if (update) {
                mintUserHoldService.lambdaUpdate()
                        .set(MintUserHold::getMintStatus, mintStatus)
                        .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                        .eq(MintUserHold::getUserAddress, tokenUser.getUserAddress())
                        .update();
            }
        }
        uUserAuthNumService.updateAuthNumStatus(tokenUser, 3, 1);
        return BaseResult.success();
    }



    @Override
    @Synchronized
    public MintUser saveMintUser(UUser uUser, MintDeploy mintDeploy, BigDecimal mintTicket, BigDecimal freeTicket) {
        MintUser mintUser = new MintUser();
        mintUser.setUserId(uUser.getId());
        mintUser.setUserAddress(uUser.getUserAddress());
        mintUser.setDeployId(mintDeploy.getId());
        mintUser.setAccord(mintDeploy.getAccord());
        mintUser.setTick(mintDeploy.getTick());
        mintUser.setMintTicket(mintTicket);
        mintUser.setFreeTicket(freeTicket);
        BigDecimal mintOne = mintDeploy.getMintOne();
        BigDecimal mintNum = mintOne.multiply(mintUser.getMintTicket());
        mintUser.setMintNum(mintNum);
        BigDecimal mintAllAll = mintDeploy.getMintAll();
        String mintRatioNode = sSystemConfigService.getByKey(SystemConfigConstant.mint_ratio_node).getConfigValue();
        String[] mintRatioNodeArr = mintRatioNode.split(",");
        String mintRatioNodeAmount = sSystemConfigService.getByKey(SystemConfigConstant.mint_ratio_node_amount).getConfigValue();
        String[] mintRatioNodeAmountArr = mintRatioNodeAmount.split(",");
        MintUser status = getOne(new QueryWrapper<MintUser>().select("sum(mint_num) as mintNum").in("status", 4));
        BigDecimal currentMintNum = BigDecimal.ZERO;
        if (status != null && status.getMintNum() != null) {
            currentMintNum = status.getMintNum().divide(mintDeploy.getMintOne(), 0);
        }
        BigDecimal surplusNum = mintUser.getMintTicket().subtract(mintUser.getFreeTicket());
        BigDecimal amount = BigDecimal.ZERO;
        if (mintUser.getMintTicket().compareTo(mintUser.getFreeTicket()) > 0) {
            if (currentMintNum.compareTo(mintAllAll) >= 0) {
                return null;
            } else {
                for (int i = 0; i < mintRatioNodeArr.length; i++) {
                    BigDecimal currentNodeMintNum = mintAllAll.multiply(new BigDecimal(mintRatioNodeArr[i]));
                    if (currentMintNum.compareTo(currentNodeMintNum) >= 0) {
                        continue;
                    }
                    BigDecimal toMintNum = currentMintNum.add(surplusNum);
                    if (toMintNum.compareTo(currentNodeMintNum) < 0) {
                        break;
                    }
                }
            }
        } else {
            mintUser.setFreeTicket(mintTicket);
        }
        mintUser.setAmount(amount);
        return mintUser;
    }

    @Override
    public String getEnStr(MintUser mintUser) {
        NonceUserMint byUserAddress = nonceUserMintService.getByUserAddress(mintUser.getUserAddress());
        Integer nonce = byUserAddress.getNonce();
        String mintPayDeadlineSecond = sSystemConfigService.getByKey(SystemConfigConstant.mint_pay_deadline_second).getConfigValue();
        BigDecimal amount = mintUser.getAmount().multiply(new BigDecimal("10").pow(18)).setScale(0, RoundingMode.DOWN);
        LandSignatureHandler landSignatureHandler = new LandSignatureHandler(mintContractAddress.toLowerCase(), chainId, "InscriptionBox", priKey);
        String signDrawAward = landSignatureHandler.userMint(mintUser.getUserAddress(), amount.toBigInteger(),  mintUser.getMintTicket().longValue(), nonce.longValue(), Long.parseLong(mintPayDeadlineSecond));
        String drawLockTime = sSystemConfigService.getByKey(SystemConfigConstant.draw_lock_time).getConfigValue();
        redisUtil.setString(RedisConstant.USER_MINT_LOCK + mintUser.getUserAddress(), mintUser.getMintTicket().toPlainString(), Long.parseLong(drawLockTime));
        return signDrawAward;
    }


    @Override
    public PageRespVO<MintMintListResp> mintList(PageReqVO pageReqVO) {
        PageRespVO<MintMintListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<MintUser> wrapper = new LambdaQueryWrapper<MintUser>().eq(MintUser::getStatus, 4).gt(MintUser::getMintNum, BigDecimal.ZERO).orderByDesc(MintUser::getUpdateTime);
        IPage<MintUser> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<MintMintListResp> respList = new ArrayList<>();
        List<MintUser> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (MintUser record : records) {
                MintMintListResp resp = new MintMintListResp();
                resp.setUserAddress(record.getUserAddress());
                resp.setMintNum(record.getMintNum().divide(new BigDecimal("10000"), 0));
                resp.setMintHash(record.getHash());
                resp.setCreateTime(record.getUpdateTime());
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

    @Override
    public PageRespVO<MintMintRankListResp> mintRankList(PageReqVO pageReqVO) {
        PageRespVO<MintMintRankListResp> respPageRespVO = new PageRespVO<>();
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        if (mintDeploy == null) {
            return respPageRespVO;
        }
        BigDecimal mintAllAll = mintDeploy.getMintAll().multiply(mintDeploy.getMintOne());
        Page<MintRankUser> page = new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize());
        page = mintUserMapper.selectMintRankPage(page, null);
        respPageRespVO.pageInit(page);
        List<MintMintRankListResp> respList = new ArrayList<>();
        List<MintRankUser> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (int i = 0; i < records.size(); i++) {
                MintMintRankListResp resp = new MintMintRankListResp();
                resp.setUserAddress(uUserService.maskAddress(records.get(i).getUserAddress()));
                resp.setMintNum(records.get(i).getMintNum().divide(new BigDecimal("10000"), 0));
                long rank = (respPageRespVO.getCurrent() - 1) * respPageRespVO.getSize() + 1 + i;
                resp.setRank(rank);
                BigDecimal divide = records.get(i).getMintNum().multiply(new BigDecimal("100")).divide(mintAllAll, 2, RoundingMode.DOWN);
                resp.setRatio(divide.toPlainString() + "%");
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


    private String getBoxNum() {
        do {
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            String english = "0123456789";
            for (int i = 0; i < 10; i++) {
                sb.append(english.charAt(random.nextInt(10)));
            }
            String number = sb.toString();
            MintUserBox byBoxNum = mintUserBoxService.getByBoxNum(number);
            if (byBoxNum == null) {
                return number;
            }
        } while (true);
    }



    @Override
    public BaseResult<String> sendJson(String privateKey, String toAddress, String data) {
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        Credentials credentials = Credentials.create(privateKey);
        Request<?, EthGetTransactionCount> nonceRequest = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST);
        try {
            EthGetTransactionCount nonceResponse = nonceRequest.send();
            if (nonceResponse.hasError()) {
                log.error("Error fetching nonce: " + nonceResponse.getError().getMessage());
                return BaseResult.fail(i18nService.getMessage("20401"));
            }
            BigInteger nonce = nonceResponse.getTransactionCount();
            log.info("Nonce for address " + credentials.getAddress() + " is: " + nonce);
            return BaseResult.success();
        } catch (Exception e) {
            return BaseResult.fail(i18nService.getMessage("20402"));
        }
    }

    @Override
    public BaseResult<MintStatusResp> mintStatus() {
        UUser tokenUser = uUserService.getTokenUser();
        String accord = "Denim-20";
        MintDeploy mintDeploy = mintDeployService.getMintDeploy(accord);
        if (mintDeploy == null) {
            return BaseResult.fail(i18nService.getMessage("20081"));
        }
        MintUserHold mintUserHold = mintUserHoldService.getMintUserHold(tokenUser.getUserAddress(), mintDeploy.getId());
        MintStatusResp resp = new MintStatusResp();
        resp.setMintStatus(mintUserHold.getMintStatus());
        return BaseResult.success(resp);
    }

    @Override
    public PageRespVO<MintMintUserListResp> mintListUser(PageReqVO pageReqVO) {
        UUser tokenUser = uUserService.getTokenUser();
        PageRespVO<MintMintUserListResp> respPageRespVO = new PageRespVO<>();
        LambdaQueryWrapper<MintUser> wrapper = new LambdaQueryWrapper<MintUser>().eq(MintUser::getUserAddress, tokenUser.getUserAddress()).gt(MintUser::getStatus, -2).orderByDesc(MintUser::getUpdateTime);
        IPage<MintUser> page = page(new Page<>(pageReqVO.getPageNum(), pageReqVO.getPageSize()), wrapper);
        respPageRespVO.pageInit(page);
        List<MintMintUserListResp> respList = new ArrayList<>();
        List<MintUser> records = page.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (MintUser record : records) {
                MintMintUserListResp resp = new MintMintUserListResp();
                BeanUtils.copyProperties(record, resp);
                resp.setMintNum(record.getMintTicket());
                respList.add(resp);
            }
        }
        respPageRespVO.setList(respList);
        return respPageRespVO;
    }

    @Override
    public BigDecimal selectAchieve(Long userId) {
        UUserBaseInfo uUserBaseInfo = uUserBaseInfoService.getUUserBaseInfo(userId);
        if (uUserBaseInfo.getIsVip() == 1 || uUserBaseInfo.getIsAdvancedVip() == 1) {
            return BigDecimal.ZERO;
        }
        BigDecimal vipAchieve = baseMapper.selectAchieveMint(userId);
        if (vipAchieve == null) {
            return BigDecimal.ZERO;
        }

        List<UUserBaseInfo> uUserBaseInfoList = uUserBaseInfoService.getVipUserList(userId);
        for (UUserBaseInfo baseInfo : uUserBaseInfoList) {
            BigDecimal vipAchieveBase = baseMapper.selectAchieveMint(baseInfo.getUserId());
            if (vipAchieveBase != null) {
                vipAchieve = vipAchieve.subtract(vipAchieveBase);
            }
        }
        return vipAchieve.multiply(BigDecimal.ZERO);
    }


}
