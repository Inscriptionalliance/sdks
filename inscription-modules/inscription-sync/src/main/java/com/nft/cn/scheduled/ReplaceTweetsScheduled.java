package com.nft.cn.scheduled;

import com.nft.cn.constant.SystemConfigConstant;
import com.nft.cn.entity.UUserTwitterToken;
import com.nft.cn.service.SSystemConfigService;
import com.nft.cn.service.TwitterService;
import com.nft.cn.service.UUserTwitterTokenService;
import com.nft.cn.util.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReplaceTweetsScheduled {

    @Autowired
    private TwitterService twitterService;
    @Autowired
    private SSystemConfigService sSystemConfigService;
    @Autowired
    private UUserTwitterTokenService uUserTwitterTokenService;

//    @Scheduled(cron = "0 0 * * * ?")
    public void replaceTweetsAward() throws Throwable {
        String officialTwitterId = sSystemConfigService.getByKey(SystemConfigConstant.official_twitter_id).getConfigValue();
        UUserTwitterToken uUserTwitterToken = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getTwitterId, officialTwitterId).one();
        BaseResult<String> stringBaseResult = twitterService.replaceTwitterToken(uUserTwitterToken);
        if (stringBaseResult.getCode() != 200) {
        } else {
            uUserTwitterToken = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getTwitterId, officialTwitterId).one();
            BaseResult<String> stringBaseResult1 = twitterService.replaceTweets(officialTwitterId, uUserTwitterToken, null);
            if (stringBaseResult1.getCode() != 200) {
            }
        }
    }

}
