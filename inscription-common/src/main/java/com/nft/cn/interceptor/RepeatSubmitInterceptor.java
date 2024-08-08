package com.nft.cn.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.nft.cn.annotation.RepeatSubmit;
import com.nft.cn.service.I18nService;
import com.nft.cn.util.BaseResult;
import com.nft.cn.util.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public abstract class RepeatSubmitInterceptor extends HandlerInterceptorAdapter
{

    @Autowired
    private I18nService i18nService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null)
            {
                if (this.isRepeatSubmit(request))
                {
                    BaseResult ajaxResult = BaseResult.fail(i18nService.getMessage("10006"));
                    ServletUtils.renderString(response, JSONObject.toJSONString(ajaxResult));
                    return false;
                }
            }
            return true;
        }
        else
        {
            return super.preHandle(request, response, handler);
        }
    }

    public abstract boolean isRepeatSubmit(HttpServletRequest request);
}
