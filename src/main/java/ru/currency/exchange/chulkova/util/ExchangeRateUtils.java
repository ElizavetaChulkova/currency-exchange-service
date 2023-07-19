package ru.currency.exchange.chulkova.util;

import ru.currency.exchange.chulkova.repository.ExchangeRateJdbcRepository;

public class ExchangeRateUtils {

    private static ExchangeRateJdbcRepository exchangeRepo = new ExchangeRateJdbcRepository();

    public static boolean isCorrectArgs(String base, String target) {
        return base.length() == 3 && target.length() == 3;
    }

    public static boolean isCorrectPair(String pair) {
        return pair.length() == 6;
    }

    public static boolean isPairExisted(String base, String target) {
        return exchangeRepo.getByCodePair(base, target).getId() != null;
    }
}
