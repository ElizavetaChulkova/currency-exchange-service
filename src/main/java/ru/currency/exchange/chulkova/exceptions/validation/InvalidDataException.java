package ru.currency.exchange.chulkova.exceptions.validation;

import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class InvalidDataException extends ValidationException {

    public InvalidDataException() {
        super(ErrorMessage.DATA_IS_INVALID);
    }
}
