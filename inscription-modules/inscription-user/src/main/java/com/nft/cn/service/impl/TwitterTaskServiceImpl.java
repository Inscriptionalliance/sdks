package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.TwitterTaskMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.TaskCheckFulfilReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TwitterTaskServiceImpl extends ServiceImpl<TwitterTaskMapper, TwitterTask> implements TwitterTaskService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private UUserTwitterTokenService uUserTwitterTokenService;
    @Autowired
    private TwitterService twitterService;
    @Autowired
    private TwitterTaskTokenService twitterTaskTokenService;
    @Autowired
    private I18nService i18nService;
    @Autowired
    private TaskIncomeService taskIncomeService;
    @Autowired
    private UUserBaseInfoService uUserBaseInfoService;

    @Override
    public BaseResult<String> checkTaskFulfil(TaskCheckFulfilReq taskCheckFulfilReq) {
        UUser tokenUser = uUserService.getTokenUser();
        TwitterTask twitterTask = lambdaQuery().eq(TwitterTask::getId, taskCheckFulfilReq.getTaskId()).eq(TwitterTask::getType, taskCheckFulfilReq.getTwitterType()).one();
        if (twitterTask == null) {
            return BaseResult.fail(i18nService.getMessage("20041"));
        }
        UUserTwitterToken uUserTwitterToken = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserAddress, tokenUser.getUserAddress()).one();
        if (uUserTwitterToken == null) {
            return BaseResult.fail(i18nService.getMessage("20034"));
        }
        BaseResult<String> stringBaseResult = BaseResult.fail(i18nService.getMessage("99999"));
        if (twitterTask.getType() == 1) {
            stringBaseResult = twitterService.checkRetweets(twitterTask.getTweetId(), uUserTwitterToken, null);
        } else if (twitterTask.getType() == 2) {
            stringBaseResult = twitterService.checkQuoteTweets(twitterTask.getTweetId(), uUserTwitterToken, null);
        } else if (twitterTask.getType() == 3) {
            stringBaseResult = twitterService.checkLiking(twitterTask.getTweetId(), uUserTwitterToken, null);
        } else {
            return BaseResult.fail(i18nService.getMessage("20040"));
        }
        if (stringBaseResult.getCode() == 200) {
            TwitterTaskToken one = twitterTaskTokenService.lambdaQuery().eq(TwitterTaskToken::getTaskId, twitterTask.getId()).eq(TwitterTaskToken::getTokenId, uUserTwitterToken.getId()).one();
            if (one == null) {
                TwitterTaskToken twitterTaskToken = new TwitterTaskToken();
                twitterTaskToken.setTaskId(twitterTask.getId());
                twitterTaskToken.setTokenId(uUserTwitterToken.getId());
                twitterTaskToken.setCreateTime(LocalDateTime.now());
                twitterTaskTokenService.save(twitterTaskToken);
                List<TaskIncome> taskIncomeList = taskIncomeService.lambdaQuery().eq(TaskIncome::getTaskId, twitterTask.getId()).eq(TaskIncome::getTaskType, 2).list();
                for (TaskIncome taskIncome : taskIncomeList) {
                    if (taskIncome.getUnit() == 1) {
                        uUserBaseInfoService.updateCredit(tokenUser.getUserAddress(), taskIncome.getIncome(), 104, null);
                    } else if (taskIncome.getUnit() == 2) {
                        uUserBaseInfoService.updateMintTicket(tokenUser.getUserAddress(), taskIncome.getIncome(), 104, null);
                    } else if (taskIncome.getUnit() == 3) {
                        uUserBaseInfoService.updateFreeTicket(tokenUser.getUserAddress(), taskIncome.getIncome(), 104, null);
                    }
                }
            }
        }
        return stringBaseResult;
    }
}
