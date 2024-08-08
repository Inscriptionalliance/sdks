package com.nft.cn.scheduled;

import com.nft.cn.service.CommunityRankService;
import com.nft.cn.service.MintRankService;
import com.nft.cn.service.TeamRankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TeamRankScheduled {

    @Autowired
    private TeamRankService teamRankService;

//    @Scheduled(cron = "0 0 * * * ?")
    public void replaceTweetsAward() throws Throwable {
        teamRankService.teamRank();
    }

}
