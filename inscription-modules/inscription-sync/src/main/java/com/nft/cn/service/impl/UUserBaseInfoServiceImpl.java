package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.UUserBaseInfoMapper;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserBaseInfo;
import com.nft.cn.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UUserBaseInfoServiceImpl extends ServiceImpl<UUserBaseInfoMapper, UUserBaseInfo> implements UUserBaseInfoService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private CreditRecordService creditRecordService;
    @Autowired
    private FreeTicketRecordService freeTicketRecordService;
    @Autowired
    private MintTicketRecordService mintTicketRecordService;
    @Autowired
    private EpicTicketRecordService epicTicketRecordService;
    @Autowired
    private RarityTicketRecordService rarityTicketRecordService;

    @Override
    public UUserBaseInfo getUUserBaseInfo(String userAddress) {
        UUserBaseInfo uUserBaseInfo = lambdaQuery().eq(UUserBaseInfo::getUserAddress, userAddress).one();
        if (uUserBaseInfo == null) {
            UUser uUser = uUserService.getByUserAddress(userAddress);
            if (uUser != null) {
                uUserBaseInfo = new UUserBaseInfo();
                uUserBaseInfo.setUserId(uUser.getId());
                uUserBaseInfo.setUserAddress(uUser.getUserAddress());
                uUserBaseInfo.setUpdateTime(LocalDateTime.now());
                uUserBaseInfo.setCreateTime(LocalDateTime.now());
                save(uUserBaseInfo);
            }
        } else {
            return uUserBaseInfo;
        }
        return lambdaQuery().eq(UUserBaseInfo::getUserAddress, userAddress).one();
    }

    @Override
    public UUserBaseInfo getUUserBaseInfo(Long userId) {
        UUserBaseInfo uUserBaseInfo = lambdaQuery().eq(UUserBaseInfo::getUserId, userId).one();
        if (uUserBaseInfo == null) {
            UUser uUser = uUserService.getById(userId);
            if (uUser != null) {
                uUserBaseInfo = new UUserBaseInfo();
                uUserBaseInfo.setUserId(uUser.getId());
                uUserBaseInfo.setUserAddress(uUser.getUserAddress());
                uUserBaseInfo.setUpdateTime(LocalDateTime.now());
                uUserBaseInfo.setCreateTime(LocalDateTime.now());
                save(uUserBaseInfo);
            }
        } else {
            return uUserBaseInfo;
        }
        return lambdaQuery().eq(UUserBaseInfo::getUserId, userId).one();
    }

    @Override
    public boolean updateFreeTicket(String userAddress, BigDecimal freeTicketNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            BigDecimal freeTicket = uUserBaseInfo.getFreeTicket();
            BigDecimal freeTicketNew = BigDecimal.ZERO;
            if (type < 200) {
                freeTicketNew = freeTicket.add(freeTicketNum);
            } else if (type > 200) {
                freeTicketNew = freeTicket.subtract(freeTicketNum);
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (freeTicketNew.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getFreeTicket, freeTicketNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getFreeTicket, freeTicket)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        freeTicketRecordService.saveFreeTicketRecord(userAddress, freeTicketNum, type, relationAddress);
        return true;
    }


    @Override
    public boolean updateMintTicket(String userAddress, BigDecimal mintTicketNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            BigDecimal mintTicket = uUserBaseInfo.getMintTicket();
            BigDecimal mintTicketNew = BigDecimal.ZERO;
            if (type < 200) {
                mintTicketNew = mintTicket.add(mintTicketNum);
            } else if (type > 200) {
                mintTicketNew = mintTicket.subtract(mintTicketNum);
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (mintTicketNew.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getMintTicket, mintTicketNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getMintTicket, mintTicket)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mintTicketRecordService.saveMintTicketRecord(userAddress, mintTicketNum, type, relationAddress);
        return true;
    }

    @Override
    public boolean updateCredit(String userAddress, BigDecimal creditNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            BigDecimal credit = uUserBaseInfo.getCredit();
            BigDecimal creditNew = BigDecimal.ZERO;
            if (type < 200) {
                creditNew = credit.add(creditNum);
            } else if (type > 200) {
                creditNew = credit.subtract(creditNum);
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (creditNew.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getCredit, creditNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getCredit, credit)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        creditRecordService.saveCreditRecord(userAddress, creditNum, type, relationAddress);
        return true;
    }

    @Override
    public boolean updateRarityTicket(String userAddress, Long rarityTicketNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            Long rarityTicket = uUserBaseInfo.getRarityTicket();
            Long rarityTicketNew = 0L;
            if (type < 200) {
                rarityTicketNew = rarityTicket + rarityTicketNum;
            } else if (type > 200) {
                rarityTicketNew = rarityTicket - rarityTicketNum;
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (rarityTicketNew < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getRarityTicket, rarityTicketNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getRarityTicket, rarityTicket)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
        }
        rarityTicketRecordService.saveRarityTicket(userAddress, rarityTicketNum, type, relationAddress);
        return true;
    }

    @Override
    public boolean updateEpicTicket(String userAddress, Long epicTicketNum, Integer type, String relationAddress) {
        while (true) {
            UUserBaseInfo uUserBaseInfo = getUUserBaseInfo(userAddress);
            if (uUserBaseInfo == null) {
                return false;
            }
            Long epicTicket = uUserBaseInfo.getEpicTicket();
            Long epicTicketNew = 0L;
            if (type < 200) {
                epicTicketNew = epicTicket + epicTicketNum;
            } else if (type > 200) {
                epicTicketNew = epicTicket - epicTicketNum;
            } else {
                throw new RuntimeException(i18nService.getMessage("99999"));
            }
            if (epicTicketNew < 0) {
                return false;
            }
            boolean update = lambdaUpdate()
                    .set(UUserBaseInfo::getEpicTicket, epicTicketNew)
                    .set(UUserBaseInfo::getUpdateTime, LocalDateTime.now())
                    .eq(UUserBaseInfo::getEpicTicket, epicTicket)
                    .eq(UUserBaseInfo::getId, uUserBaseInfo.getId())
                    .update();
            if (update) {
                break;
            }
        }
        epicTicketRecordService.saveEpicTicket(userAddress, epicTicketNum, type, relationAddress);
        return true;
    }

}
