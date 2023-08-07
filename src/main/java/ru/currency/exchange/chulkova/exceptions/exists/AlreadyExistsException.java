package ru.currency.exchange.chulkova.exceptions.exists;

import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class AlreadyExistsException extends ApplicationException {

    public AlreadyExistsException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
