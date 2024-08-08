package com.nft.cn.scheduled;

import com.nft.cn.service.MintUserService;
import com.nft.cn.service.UUserAuthNumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserAuthNumStatusScheduled {

    @Autowired
    private UUserAuthNumService uUserAuthNumService;

//    @Scheduled(cron = "0 * * * * ?")
    public void userAuthNumStatusScheduled() throws Throwable {
        uUserAuthNumService.userAuthNumStatusScheduled();
    }


}
