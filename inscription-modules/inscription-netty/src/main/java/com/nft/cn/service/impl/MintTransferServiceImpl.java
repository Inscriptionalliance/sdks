package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintTransferMapper;
import com.nft.cn.entity.MintTransfer;
import com.nft.cn.entity.MintUserHold;
import com.nft.cn.entity.UUserAuthNum;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.MintTransferAuthErrorReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MintTransferServiceImpl extends ServiceImpl<MintTransferMapper, MintTransfer> implements MintTransferService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private MintUserHoldService mintUserHoldService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private UUserAuthNumService uUserAuthNumService;


    @Override
    public BaseResult<String> transferAuthError(String userAddress, MintTransferAuthErrorReq mintTransferAuthErrorReq) {
        UUserAuthNum authNum = uUserAuthNumService.getAuthNum(mintTransferAuthErrorReq.getAuthNum(), 3);
        if (authNum == null) {
            return BaseResult.fail(i18nService.getMessage("20215"));
        }
        if (authNum.getPayBoxAuthNumStatus() != 2) {
            if (authNum.getPayBoxAuthNumStatus() == 1) {
                return BaseResult.fail(i18nService.getMessage("20300"));
            } else if (authNum.getPayBoxAuthNumStatus() == 3) {
                return BaseResult.fail(i18nService.getMessage("20214"));
            } else if (authNum.getPayBoxAuthNumStatus() == 4) {
                return BaseResult.fail(i18nService.getMessage("20215"));
            } else {
                return BaseResult.fail(i18nService.getMessage("99999"));
            }
        }
        mintUserHoldService.lambdaUpdate()
                .set(MintUserHold::getMintTransferStatus, 1)
                .set(MintUserHold::getUpdateTime, LocalDateTime.now())
                .eq(MintUserHold::getUserAddress, userAddress)
                .update();
        uUserAuthNumService.updateAuthNumStatus(userAddress, 3, 3);
        return BaseResult.success();
    }
}
