package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.TwitterTaskTokenMapper;
import com.nft.cn.entity.*;
import com.nft.cn.service.*;
import com.nft.cn.util.BaseResult;
import com.nft.cn.vo.req.TaskCheckFulfilReq;
import com.nft.cn.vo.resp.CommunityLikeDataResp;
import com.nft.cn.vo.resp.TaskIncomeResp;
import com.nft.cn.vo.resp.TwitterTaskListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterTaskTokenServiceImpl extends ServiceImpl<TwitterTaskTokenMapper, TwitterTaskToken> implements TwitterTaskTokenService {

    @Autowired
    private UUserService uUserService;
    @Autowired
    private TwitterTaskService twitterTaskService;
    @Autowired
    private SelfTaskService selfTaskService;
    @Autowired
    private TwitterTaskTokenService twitterTaskTokenService;
    @Autowired
    private UUserTwitterTokenService uUserTwitterTokenService;
    @Autowired
    private TaskIncomeService taskIncomeService;
    @Autowired
    private CommunityLikeService communityLikeService;
    @Autowired
    private UUserRefereeService uUserRefereeService;

    @Override
    public BaseResult<List<TwitterTaskListResp>> getTwitterTaskListResp() {
        UUser tokenUser = uUserService.getTokenUser();
        List<TwitterTask> twitterTaskList = twitterTaskService.list();
        List<SelfTask> selfTaskList = selfTaskService.list();
        UUserTwitterToken uUserTwitterToken = uUserTwitterTokenService.lambdaQuery().eq(UUserTwitterToken::getUserAddress, tokenUser.getUserAddress()).one();
        List<TwitterTaskToken> twitterTaskTokenList = new ArrayList<>();
        if (uUserTwitterToken != null) {
            twitterTaskTokenList = twitterTaskTokenService.lambdaQuery().eq(TwitterTaskToken::getTokenId, uUserTwitterToken.getId()).list();
        }
        UUserReferee uUserReferee = uUserRefereeService.selectByUserAddress(tokenUser.getUserAddress());
        List<TwitterTaskListResp> respList = new ArrayList<>();
        for (TwitterTask twitterTask : twitterTaskList) {
            TwitterTaskListResp resp = new TwitterTaskListResp();
            resp.setTaskId(twitterTask.getId());
            resp.setTwitterType(twitterTask.getType());
            resp.setTweetId(twitterTask.getTweetId());
            resp.setValid(twitterTask.getValid());
            resp.setDepict(twitterTask.getDepict());
            resp.setTwitterUserId(twitterTask.getTwitterUserId());
            resp.setTwitterUsername(twitterTask.getTwitterUsername());
            resp.setTaskType(2);
            List<TwitterTaskToken> twitterTaskTokens = twitterTaskTokenList.stream().filter(twitterTaskToken -> twitterTaskToken.getTaskId().equals(twitterTask.getId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(twitterTaskTokens)) {
                resp.setFulfil(1);
            }
            List<TaskIncome> taskIncomeList = taskIncomeService.lambdaQuery().eq(TaskIncome::getTaskType, 2).eq(TaskIncome::getTaskId, twitterTask.getId()).list();
            List<TaskIncomeResp> incomeRespList = new ArrayList<>();
            for (TaskIncome taskIncome : taskIncomeList) {
                TaskIncomeResp incomeResp = new TaskIncomeResp();
                incomeResp.setIncome(taskIncome.getIncome());
                incomeResp.setTaskId(taskIncome.getTaskId());
                incomeResp.setTaskType(2);
                incomeResp.setUnit(taskIncome.getUnit());
                incomeRespList.add(incomeResp);
            }
            resp.setTaskIncomeRespList(incomeRespList);
            respList.add(resp);
        }
        for (SelfTask selfTask : selfTaskList) {
            TwitterTaskListResp resp = new TwitterTaskListResp();
            resp.setTaskId(selfTask.getId());
            resp.setTwitterType(selfTask.getType());
            resp.setValid(selfTask.getValid());
            resp.setDepict(selfTask.getDepict());
            resp.setTaskType(1);
            resp.setFulfil(uUserReferee.getIncomeStatus());
            respList.add(resp);
        }
        return BaseResult.success(respList);
    }

    @Override
    public BaseResult<CommunityLikeDataResp> communityLikeData() {
        UUser tokenUser = uUserService.getTokenUser();
        LocalDate now = LocalDate.now();
        Integer continuousLikeNum = 0;
        Integer continuousLikeNumToday = 0;
        int todayOfWeek = now.getDayOfWeek().getValue();
        for (int i = 0; i < todayOfWeek; i++) {
            LocalDate afterDay = now.minusDays(i);
            continuousLikeNum = communityLikeService.lambdaQuery().eq(CommunityLike::getUserAddress, tokenUser.getUserAddress()).ge(CommunityLike::getCreateTime, afterDay).count();
            if (i == 0) {
                continuousLikeNumToday = continuousLikeNum;
            }
            if (i > 0 && (continuousLikeNum - continuousLikeNumToday) < i) {
                break;
            }
        }
        CommunityLikeDataResp resp = new CommunityLikeDataResp();
        resp.setContinuousLikeNum(continuousLikeNum);
        return BaseResult.success(resp);
    }
}
