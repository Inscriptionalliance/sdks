package com.nft.cn.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class SysUtil {
    public static String getRemoteIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        log.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            log.info("getIpAddress(HttpServletRequest) - X-Real-IP - String ip=" + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            log.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
        }

        return ip;
    }

    public static String createPaySerialNum() {

        int machineId = 1;

        int hashCodeV = UUID.randomUUID().toString().hashCode();

        if (hashCodeV < 0) {

            hashCodeV = -hashCodeV;

        }
        return machineId + String.format("%014d", hashCodeV);
    }

    public static String createOrderNo() {
        SimpleDateFormat sfDate = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sfDate.format(new Date());
        String random = getRandom620(3);

        return strDate + random;
    }


    public static String getRandom620(Integer length) {
        String result = "";
        Random rand = new Random();
        int n = 20;
        if (null != length && length > 0) {
            n = length;
        }
        int randInt = 0;
        for (int i = 0; i < n; i++) {
            randInt = rand.nextInt(10);
            result += randInt;
        }
        return result;
    }

}
