package ru.currency.exchange.chulkova.util;

import ru.currency.exchange.chulkova.repository.CurrencyJdbcRepository;

public class CurrencyUtils {

    private static CurrencyJdbcRepository currencyRepo = new CurrencyJdbcRepository();

    public static boolean isNotValid(String code, String name, String sign) {
        return code.length() != 3 || name.length() > 100 || sign.length() > 5;
    }

    public static boolean isExisted(String code) {
        return currencyRepo.getByCode(code).get().getId() != null;
    }
}
