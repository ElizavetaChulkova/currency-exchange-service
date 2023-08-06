package ru.currency.exchange.chulkova;

import ru.currency.exchange.chulkova.model.Currency;
import ru.currency.exchange.chulkova.model.ExchangeRate;

import java.util.List;

public class TestData {

    public static final Currency currency1 = new Currency(1, "AUD", "Australian dollar", "A$");
    public static final String CODE = currency1.getCode();
    public static final int CURRENCY_ID = currency1.getId();
    public static final Currency currency2 = new Currency(2, "USD", "US dollar", "$");
    public static final Currency currency3 = new Currency(3, "RUB", "Russian ruble", "₽");
    public static final Currency currency4 = new Currency(4, "EUR", "Euro", "€");
    public static final Currency CURRENCY_TO_CREATE = new Currency(null, "COD", "To create", "S");
    public static final Currency CURRENCY_TO_UPDATE = new Currency(null, "UPD", "To update", "U");
    public static final List<Currency> currencies = List.of(currency1, currency2, currency3, currency4);
    public static final ExchangeRate rate1 = new ExchangeRate(1, currency2, currency3, 90.59);
    public static final int RATE_ID = rate1.getId();
    public static final String BASE = rate1.getBase().getCode();
    public static final String TARGET = rate1.getTarget().getCode();
    public static final ExchangeRate rate2 = new ExchangeRate(2, currency2, currency4, 0.89);
    public static final ExchangeRate RATE_TO_CREATE = new ExchangeRate(null, currency1, currency2, 1.0);
    public static final ExchangeRate RATE_TO_UPDATE = new ExchangeRate(null, currency1, currency3, 1.1);
    public static final List<ExchangeRate> rates = List.of(rate1, rate2);
    public static final String INVALID_CODE = "NOT";
    public static final int INVALID_ID = 100000;

    public static final String EXCHANGE_FROM = "USD";
    public static final String EXCHANGE_TO = "RUB";
    public static final String EXCHANGE_TO_EXCEPTION = "AUD";
    public static final String EXCHANGE_AMOUNT = "1.0";
    public static final double STRAIGHT_RESULT = 90.59;
    public static final double REVERSED_RESULT = 0.011;
    public static final double USD_CROSS_RESULT = 0.0098;
    public static final String EXCHANGE_CROSS_FROM = "EUR";
}
