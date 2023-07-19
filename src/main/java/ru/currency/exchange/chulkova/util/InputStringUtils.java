package ru.currency.exchange.chulkova.util;

import javax.servlet.http.HttpServletRequest;

public class InputStringUtils {

    public static String parsePathInfo(HttpServletRequest request) {
        return request.getPathInfo().replace("/", "").toUpperCase();
    }

    public static boolean isEmptyField(String s1, String s2, String s3) {
        return s1.isEmpty() || s2.isEmpty() || s3.isEmpty();
    }
}
