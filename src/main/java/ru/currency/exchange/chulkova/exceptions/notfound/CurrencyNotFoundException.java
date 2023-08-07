package ru.currency.exchange.chulkova.exceptions.notfound;

import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class CurrencyNotFoundException extends NotFoundException {

    public CurrencyNotFoundException() {
        super(ErrorMessage.CURRENCY_NOT_FOUND);
    }
}
