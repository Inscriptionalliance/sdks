package com.nft.cn.scheduled;

import com.nft.cn.service.MintCommunityPaidAchieveService;
import com.nft.cn.service.MintCommunityTeamAchieveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MintCommunityPaidScheduled {

    @Autowired
    private MintCommunityPaidAchieveService mintCommunityPaidAchieveService;


//    @Scheduled(fixedDelay = 3000)
    public void mintCommunityPaidScheduled() throws Throwable {
        mintCommunityPaidAchieveService.mintCommunityPaidScheduled();
    }

}
