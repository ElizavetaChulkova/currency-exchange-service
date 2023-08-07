package ru.currency.exchange.chulkova.exceptions.exists;

import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class CurrencyAlreadyExistsException extends AlreadyExistsException {

    public CurrencyAlreadyExistsException() {
        super(ErrorMessage.ALREADY_EXISTS);
    }
}
