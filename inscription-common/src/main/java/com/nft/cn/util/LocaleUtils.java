package com.nft.cn.util;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class LocaleUtils {

    private LocaleUtils() {
    }

    public static Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }
}