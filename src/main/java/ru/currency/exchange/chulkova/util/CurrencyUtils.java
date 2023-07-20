package ru.currency.exchange.chulkova.util;

import ru.currency.exchange.chulkova.service.CurrencyService;

public class CurrencyUtils {

    private static final CurrencyService service = new CurrencyService();

    public static boolean isNotValid(String code, String name, String sign) {
        return code.length() != 3 || name.length() > 100 || sign.length() > 5;
    }
}
