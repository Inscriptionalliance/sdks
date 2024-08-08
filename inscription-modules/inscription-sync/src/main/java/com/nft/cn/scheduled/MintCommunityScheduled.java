package com.nft.cn.scheduled;

import com.nft.cn.service.MintCommunityTeamAchieveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class MintCommunityScheduled {

    @Autowired
    private MintCommunityTeamAchieveService mintCommunityTeamAchieveService;


//    @Scheduled(fixedDelay = 3000)
    public void mintCommunityScheduled() throws Throwable {
        mintCommunityTeamAchieveService.mintCommunityScheduled();
    }

}
