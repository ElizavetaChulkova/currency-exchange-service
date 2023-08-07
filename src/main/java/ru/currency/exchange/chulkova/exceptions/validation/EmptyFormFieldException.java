package ru.currency.exchange.chulkova.exceptions.validation;

import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class EmptyFormFieldException extends ValidationException {

    public EmptyFormFieldException() {
        super(ErrorMessage.EMPTY_FORM_FIELD);
    }
}
