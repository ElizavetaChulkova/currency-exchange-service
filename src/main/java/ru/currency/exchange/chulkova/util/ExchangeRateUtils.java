package ru.currency.exchange.chulkova.util;

import ru.currency.exchange.chulkova.repository.CurrencyJdbcRepository;
import ru.currency.exchange.chulkova.repository.ExchangeRateJdbcRepository;
import ru.currency.exchange.chulkova.service.ExchangeRateService;

public class ExchangeRateUtils {

    private static ExchangeRateJdbcRepository exchangeRepo = new ExchangeRateJdbcRepository();
    private static CurrencyJdbcRepository currencyRepo = new CurrencyJdbcRepository();
    private static ExchangeRateService service = new ExchangeRateService();

    public static boolean isCorrectArgs(String base, String target) {
        return base.length() == 3 && target.length() == 3;
    }

    public static boolean isCorrectPair(String pair) {
        return pair.length() == 6;
    }

    public static boolean isPairNotExisted(String base, String target) {
        return service.getByCodePair(base, target).getId() == null;
    }
}
