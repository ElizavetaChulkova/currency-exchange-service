package ru.currency.exchange.chulkova.exceptions;

public class ValidationException extends ApplicationException {
    public ValidationException(ErrorMessage error) {
        super(error);
    }
}
