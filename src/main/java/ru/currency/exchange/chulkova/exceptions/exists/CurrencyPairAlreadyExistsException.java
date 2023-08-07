package ru.currency.exchange.chulkova.exceptions.exists;

import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class CurrencyPairAlreadyExistsException extends AlreadyExistsException {

    public CurrencyPairAlreadyExistsException() {
        super(ErrorMessage.PAIR_ALREADY_EXISTS);
    }
}
