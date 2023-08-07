package ru.currency.exchange.chulkova.util;

import ru.currency.exchange.chulkova.exceptions.validation.CodeNotInAddressException;
import ru.currency.exchange.chulkova.exceptions.validation.EmptyFormFieldException;
import ru.currency.exchange.chulkova.exceptions.validation.InvalidDataException;

public class ValidationUtil {

    public static void validateCurrency(String code, String name, String sign) {
        if (InputStringUtils.isEmptyField(code, name, sign)) {
            throw new EmptyFormFieldException();
        } else if (CurrencyUtils.isNotValid(code, name, sign) || code.length() != 3) {
            throw new InvalidDataException();
        }
    }

    public static void validateCurrency(String code) {
        if (code.isEmpty()) {
            throw new CodeNotInAddressException();
        } else if (code.length() != 3) {
            throw new InvalidDataException();
        }
    }

    public static void validateExchangeRate(String base, String target, String rate) {
        if (InputStringUtils.isEmptyField(base, target, rate)) {
            throw new EmptyFormFieldException();
        } else if (!ExchangeRateUtils.isCorrectArgs(base, target)) {
            throw new InvalidDataException();
        }
    }

    public static void validateExchangeRate(String pair) {
        if (pair.isEmpty()) {
            throw new CodeNotInAddressException();
        } else if (!ExchangeRateUtils.isCorrectPair(pair)) {
            throw new InvalidDataException();
        }
    }

    public static void validateExchange(String from, String to, String amount) {
        if (InputStringUtils.isEmptyField(from, to, amount)) {
            throw new EmptyFormFieldException();
        } else if (!ExchangeRateUtils.isCorrectArgs(from, to)) {
            throw new InvalidDataException();
        }
    }
}
