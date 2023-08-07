package ru.currency.exchange.chulkova.exceptions.notfound;

import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class NotFoundException extends ApplicationException {

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
