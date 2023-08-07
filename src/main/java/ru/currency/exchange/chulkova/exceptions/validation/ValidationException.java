package ru.currency.exchange.chulkova.exceptions.validation;

import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class ValidationException extends ApplicationException {
    public ValidationException(ErrorMessage error) {
        super(error);
    }
}
