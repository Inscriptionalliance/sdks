package com.nft.cn.service;

import com.nft.cn.entity.UUserTwitterToken;
import com.nft.cn.util.BaseResult;

import javax.servlet.http.HttpServletRequest;

public interface TwitterService {

    void sendTweet(String content, String bearerToken);

    BaseResult<String> checkOauth2(String userAddress);

    BaseResult<String> replaceTwitterToken(UUserTwitterToken uUserTwitterToken);

    BaseResult<String> replaceTweets(String officialTwitterId, UUserTwitterToken uUserTwitterToken, String pageToken);

    BaseResult<String> checkRetweets(String tweetId, UUserTwitterToken uUserTwitterToken, String pageToken);

    BaseResult<String> checkQuoteTweets(String tweetId, UUserTwitterToken uUserTwitterToken, String pageToken);

    BaseResult<String> checkLiking(String tweetId, UUserTwitterToken uUserTwitterToken, String pageToken);

    BaseResult<String> checkFollows(String twitterId, UUserTwitterToken uUserTwitterToken);

}