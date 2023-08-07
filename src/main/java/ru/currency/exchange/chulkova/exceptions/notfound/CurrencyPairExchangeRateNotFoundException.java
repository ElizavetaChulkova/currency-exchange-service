package ru.currency.exchange.chulkova.exceptions.notfound;

import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class CurrencyPairExchangeRateNotFoundException extends NotFoundException {

    public CurrencyPairExchangeRateNotFoundException() {
        super(ErrorMessage.PAIR_EXCHANGE_RATE_NOT_FOUND);
    }
}
