package com.nft.cn.scheduled;

import com.nft.cn.service.MintRankUserService;
import com.nft.cn.service.MintUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MintStatusScheduled {

    @Autowired
    private MintUserService mintUserService;

//    @Scheduled(cron = "0 * * * * ?")
    public void mintStatusScheduled() throws Throwable {
        mintUserService.mintStatusScheduled();
    }


}
