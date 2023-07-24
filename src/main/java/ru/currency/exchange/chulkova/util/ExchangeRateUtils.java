package ru.currency.exchange.chulkova.util;

public class ExchangeRateUtils {

    public static boolean isCorrectArgs(String base, String target) {
        return base.length() == 3 && target.length() == 3;
    }

    public static boolean isCorrectPair(String pair) {
        return pair.length() == 6;
    }
}
