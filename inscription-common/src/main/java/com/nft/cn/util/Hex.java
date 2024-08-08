package com.nft.cn.util;

import java.math.BigInteger;

public class Hex {

    public static String toHexNumber(BigInteger integer) {

        return integer.toString(16);

    }

    public static String hexToString(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i+1), 16));
        }
        return new String(bytes);
    }

    public static String stringToHex(String input) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int decimal = input.charAt(i);
            hexString.append(Integer.toString(decimal, 16));
        }
        return hexString.toString();
    }


}
