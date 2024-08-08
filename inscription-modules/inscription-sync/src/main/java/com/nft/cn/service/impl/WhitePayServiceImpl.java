package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.dao.WhitePayMapper;
import com.nft.cn.entity.UUser;
import com.nft.cn.entity.UUserBaseInfo;
import com.nft.cn.entity.WhitePay;
import com.nft.cn.service.SSystemConfigService;
import com.nft.cn.service.UUserBaseInfoService;
import com.nft.cn.service.UUserService;
import com.nft.cn.service.WhitePayService;
import com.nft.cn.util.HtRPCApiUtils;
import com.nft.cn.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
public class WhitePayServiceImpl extends ServiceImpl<WhitePayMapper, WhitePay> implements WhitePayService {

    @Override
    public void syncPayWhite(String data, String hash) {
    }
}
