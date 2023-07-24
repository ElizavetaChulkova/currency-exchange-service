package ru.currency.exchange.chulkova.exceptions;

public class AlreadyExistsException extends ApplicationException {

    public AlreadyExistsException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
