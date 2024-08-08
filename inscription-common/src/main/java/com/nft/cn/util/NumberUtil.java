package com.nft.cn.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class NumberUtil {

    public static BigDecimal covert(String content) {
        BigDecimal number = BigDecimal.ZERO;
        String[] HighLetter = {"a", "b", "c", "d", "e", "f"};
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i <= 9; i++) {
            map.put(i + "", i);
        }
        for (int j = 10; j < HighLetter.length + 10; j++) {
            map.put(HighLetter[j - 10], j);
        }
        String[] str = new String[content.length()];
        for (int i = 0; i < str.length; i++) {
            str[i] = content.substring(i, i + 1);
        }
        for (int i = 0; i < str.length; i++) {
            number = number.add(new BigDecimal(map.get(str[i]) * Math.pow(16, str.length - 1 - i)));
        }
        return number;
    }

    public static String getData(String data,int x,int y){
        String s = data.substring(2);
        return s.substring(x,y);
    }
}
