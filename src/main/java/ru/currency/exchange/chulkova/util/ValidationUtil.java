package ru.currency.exchange.chulkova.util;

import ru.currency.exchange.chulkova.exceptions.ValidationException;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.*;

public class ValidationUtil {

    public static void validateCurrency(String code, String name, String sign) {
        if (InputStringUtils.isEmptyField(code, name, sign)) {
            throw new ValidationException(EMPTY_FORM_FIELD);
        } else if (CurrencyUtils.isNotValid(code, name, sign) || code.length() != 3) {
            throw new ValidationException(DATA_IS_INVALID);
        }
    }

    public static void validateCurrency(String code) {
        if (code.isEmpty()) {
            throw new ValidationException(CODE_NOT_IN_ADDRESS);
        } else if (code.length() != 3) {
            throw new ValidationException(DATA_IS_INVALID);
        }
    }

    public static void validateExchangeRate(String base, String target, String rate) {
        if (InputStringUtils.isEmptyField(base, target, rate)) {
            throw new ValidationException(EMPTY_FORM_FIELD);
        } else if (!ExchangeRateUtils.isCorrectArgs(base, target)) {
            throw new ValidationException(DATA_IS_INVALID);
        }
    }

    public static void validateExchangeRate(String pair) {
        if (pair.isEmpty()) {
            throw new ValidationException(CODE_NOT_IN_ADDRESS);
        } else if (!ExchangeRateUtils.isCorrectPair(pair)) {
            throw new ValidationException(DATA_IS_INVALID);
        }
    }

    public static void validateExchange(String from, String to, String amount) {
        if (InputStringUtils.isEmptyField(from, to, amount)) {
            throw new ValidationException(EMPTY_FORM_FIELD);
        } else if (!ExchangeRateUtils.isCorrectArgs(from, to)) {
            throw new ValidationException(DATA_IS_INVALID);
        }
    }
}
