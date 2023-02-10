package com.goit.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

public class TimeZoneQueryParameter {
    public String parseTimeZone(HttpServletRequest request) {
        String defaultTimeZone = "UTC";

        String result = (request.getCookies() == null) ? defaultTimeZone :
                Stream.of(request.getCookies())
                .filter(c -> "lastTimezone".equals(c.getName()))
                .map(Cookie::getValue)
                .findAny().orElseGet(() -> defaultTimeZone);

        if (request.getParameterMap().containsKey("timezone")) {
            return (request.getParameter("timezone").replace(" ", "+").length() < 1) ?
                    result : request.getParameter("timezone").replace(" ", "+").toUpperCase();
        }

        return result;
    }
}
