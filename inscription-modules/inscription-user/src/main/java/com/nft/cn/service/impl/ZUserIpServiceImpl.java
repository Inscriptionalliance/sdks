package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.ZUserIpMapper;
import com.nft.cn.entity.UUserReferee;
import com.nft.cn.entity.ZUserIp;
import com.nft.cn.service.ZUserIpService;
import com.nft.cn.util.SysUtil;
import com.nft.cn.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ZUserIpServiceImpl extends ServiceImpl<ZUserIpMapper, ZUserIp> implements ZUserIpService {

    @Override
    public void saveUserIp(UUserReferee uUserReferee) {
        try {
            String remoteIp = SysUtil.getRemoteIp(TokenUtil.getRequest());
            ZUserIp zUserIp = new ZUserIp();
            zUserIp.setUserAddress(uUserReferee.getUserAddress());
            zUserIp.setRefereeAddress(uUserReferee.getRefereeUserAddress());
            zUserIp.setUserIp(remoteIp);
            zUserIp.setCreateTime(LocalDateTime.now());
            save(zUserIp);
        } catch (Exception e) {
        }

    }
}
