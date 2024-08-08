package com.nft.cn.scheduled;

import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.entity.UUserTwitterToken;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommunityRankScheduled {

    @Autowired
    private CommunityRankService communityRankService;

//    @Scheduled(cron = "0 0 * * * ?")
    public void replaceTweetsAward() throws Throwable {
        communityRankService.communityRank();
    }

}
