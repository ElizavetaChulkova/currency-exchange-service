package ru.currency.exchange.chulkova.util;

public class CurrencyUtils {

    public static boolean isNotValid(String code, String name, String sign) {
        return code.length() != 3 || name.length() > 100 || sign.length() > 5;
    }
}
