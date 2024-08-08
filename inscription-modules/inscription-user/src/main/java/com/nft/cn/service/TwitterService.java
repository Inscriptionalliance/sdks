package com.nft.cn.service;

import com.nft.cn.entity.UUserTwitterToken;
import com.nft.cn.util.BaseResult;

import javax.servlet.http.HttpServletRequest;

public interface TwitterService {

    String oauth2Url(Long userId);

    String oauth2Url();
    BaseResult<String> oauth2CallBack(HttpServletRequest request);

    void sendTweet(String content, String bearerToken);

    BaseResult<String> checkOauth2();

    BaseResult<String> checkRetweets(String tweetId, UUserTwitterToken uUserTwitterToken, String pageToken);

    BaseResult<String> checkQuoteTweets(String tweetId, UUserTwitterToken uUserTwitterToken, String pageToken);

    BaseResult<String> checkLiking(String tweetId, UUserTwitterToken uUserTwitterToken, String pageToken);

    BaseResult<String> checkFollows(String twitterId, UUserTwitterToken uUserTwitterToken);

}