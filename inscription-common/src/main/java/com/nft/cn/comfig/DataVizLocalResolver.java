package com.nft.cn.comfig;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DataVizLocalResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
    List<Locale> LOCALES = Arrays.asList(
            new Locale("en"),
            new Locale("zh"), new Locale("cn"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("lang");
        return headerLang == null || headerLang.isEmpty()
                ? Locale.CHINESE
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
    }
}
