package com.nft.cn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.RedisConstant;
import com.nft.cn.dao.MintUserMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.Hex;
import com.nft.cn.util.HtRPCApiUtils;
import com.nft.cn.util.NumberUtil;
import com.nft.cn.util.RedisUtil;
import com.nft.cn.vo.DataSyncVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class MintUserServiceImpl extends ServiceImpl<MintUserMapper, MintUser> implements MintUserService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private NonceUserMintService nonceUserMintService;
    @Autowired
    private MintUserHoldService mintUserHoldService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;
    @Autowired
    private MintUserBoxService mintUserBoxService;
    @Autowired
    private UUserAuthNumService uUserAuthNumService;
    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintUserPayBoxService mintUserPayBoxService;

    @Override
    public void userMintSync(DataSyncVO dataSyncVO, String hash, Long blockNum, String mintContractAddress) {
        String userAddress = HtRPCApiUtils.getridof_zero_address(NumberUtil.getData(dataSyncVO.getData(), 0, 64));
        Long dataId = Long.valueOf(new BigDecimal(HtRPCApiUtils.toNum(NumberUtil.getData(dataSyncVO.getData(), 64, 128))).toString());
        MintUser mintUser = lambdaQuery().eq(MintUser::getUserAddress, userAddress).in(MintUser::getStatus, -2, 1).one();
        if (mintUser == null) {
            int millis = 0;
            while (true) {
                millis = millis + 2000;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mintUser = lambdaQuery().eq(MintUser::getUserAddress, userAddress).in(MintUser::getStatus, -2, 1).one();
                if (mintUser != null) {
                    break;
                }
                if (millis > 19000) {
                    return;
                }
            }
        }
        if (mintUser.getFreeTicket().compareTo(BigDecimal.ZERO) > 0) {
            boolean freeTicketFlag = uUserBaseInfoService.updateFreeTicket(userAddress, mintUser.getFreeTicket(), 208, null);
            if (!freeTicketFlag) {
                return;
            }
        }
        if (mintUser.getMintTicket().compareTo(BigDecimal.ZERO) > 0) {
            boolean mintTicketFlag = uUserBaseInfoService.updateMintTicket(userAddress, mintUser.getMintTicket(), 208, null);
            if (!mintTicketFlag) {
                return;
            }
        }
        MintUserBox mintUserBox = mintUserBoxService.lambdaQuery().eq(MintUserBox::getUserAddress, userAddress).one();
        if (mintUserBox == null) {
            String boxNum = getBoxNum();
            mintUserBox = new MintUserBox();
            mintUserBox.setUserId(mintUser.getId());
            mintUserBox.setUserAddress(mintUser.getUserAddress());
            mintUserBox.setNum(boxNum);
            mintUserBox.setAccord("Denim-20");
            mintUserBox.setCreateTime(LocalDateTime.now());
            mintUserBox.setUpdateTime(LocalDateTime.now());
            mintUserBoxService.save(mintUserBox);
        }
        mintUser.setPayHash(hash);
        mintUser.setStatus(2);
        mintUser.setUpdateTime(LocalDateTime.now());
        updateById(mintUser);
        mintUserHoldService.lambdaUpdate().set(MintUserHold::getMintStatus, 2).set(MintUserHold::getUpdateTime, LocalDateTime.now()).eq(MintUserHold::getUserAddress, userAddress).update();
        nonceUserMintService.updateNonce(userAddress);
        uUserAuthNumService.updateAuthNumStatus(userAddress, 2, 1);
        if (redisUtil.getString(RedisConstant.USER_MINT_LOCK + userAddress) != null) {
            redisUtil.delete(RedisConstant.USER_MINT_LOCK + userAddress );
        }
    }

    @Override
    public void userMintSyncTest(DataSyncVO dataSyncVO, String hash, Long blockNum, String mintContractAddress) {

    }

    @Override
    public void userMintSynctest(DataSyncVO dataSyncVO, String hash, Long blockNum, String mintContractAddress) {
    }

    @Override
    public void mintStatusScheduled() {

    }

    @Override
    public BigDecimal sumMintNum(Long userId, Integer status) {
        return baseMapper.sumMintNum(userId, status);
    }

    @Override
    public BigDecimal sumTeamMintNum(Long userId, Integer status) {
        return baseMapper.sumTeamMintNum(userId, status);
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

}
