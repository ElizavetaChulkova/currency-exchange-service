package ru.currency.exchange.chulkova.exceptions;

public class NotFoundException extends ApplicationException {

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
