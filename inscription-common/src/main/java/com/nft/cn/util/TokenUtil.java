package com.nft.cn.util;

import com.nft.cn.exception.LoginException;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

	public static String getTokenUserId() throws Exception {
		String token = getRequest().getHeader("token");
		if(StringUtils.isEmpty(token)){
			throw new LoginException("ee");
		}
		String userId = JwtUtil.parseJWT(token).getId();
		return userId;
	}

	public static String getTokenUserIdNoLogin() throws Exception {
		String token = getRequest().getHeader("token");
		if(StringUtils.isEmpty(token)){
			return "";
		}
		String userId = JwtUtil.parseJWT(token).getId();
		return userId;
	}

	public static Claims getTokenUserInfo(String token) throws Exception {
		return JwtUtil.parseJWT(token);
	}

	public static Claims getTokenUserInfo() throws Exception {
		String token = getRequest().getHeader("token");
		return JwtUtil.parseJWT(token);
	}

	public static String getUserId() throws Exception {
		String token = getRequest().getHeader("token");
		return JwtUtil.parseJWT(token).getId();
	}

	public static String getLang() throws Exception {
		String token = getRequest().getHeader("lang");
		return token;
	}

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return requestAttributes == null ? null : requestAttributes.getRequest();
	}
}
