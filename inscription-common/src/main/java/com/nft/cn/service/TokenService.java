package com.nft.cn.service;

import com.alibaba.fastjson.JSONObject;
import com.nft.cn.util.JwtUtil;
import com.nft.cn.util.TokenUtil;
import org.springframework.stereotype.Service;

@Service("TokenService")
public class TokenService {

    public String getToken(Long userId,Object user) {

        String token = "";
        try {
            token = JwtUtil.createJWT(userId.toString(), JSONObject.toJSON(user).toString(), JwtUtil.JWT_TTL);
        }catch (Exception e){

        }
        return token;
    }


    public String getLang() {

        String lang = "";
        try {
            lang = TokenUtil.getLang();
        }catch (Exception e){

        }
        return lang;
    }




}
