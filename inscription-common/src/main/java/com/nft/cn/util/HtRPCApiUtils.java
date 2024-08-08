package com.nft.cn.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Slf4j
@Component
public class HtRPCApiUtils {



    public static BigInteger toNum(String hexString) {
        hexString = hexString.substring(2);
        return new BigInteger(hexString, 16);
    }

    public static String getridof_zero_address(String input){
        if (input.length() < 40) {
            return null;
        }
        String str = input.substring(input.length() - 40, input.length());
        return "0x" + str;
    }

    public static String getridof_zero(String input) {
        String str = input.replaceFirst("^0*", "");
        return "0x" + str;
    }

}
