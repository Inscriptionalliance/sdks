package com.nft.cn.scheduled;

import com.nft.cn.service.BtiaPledgeService;
import com.nft.cn.service.MintRankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BtiaPledgeScheduled {

    @Autowired
    private BtiaPledgeService btiaPledgeService;

//    @Scheduled(cron = "0 0 0 * * ?")
    public void btiaPledgeAward() throws Throwable {
        btiaPledgeService.btiaPledgeAward();
    }

}
