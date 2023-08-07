package ru.currency.exchange.chulkova.exceptions.validation;

import ru.currency.exchange.chulkova.exceptions.ErrorMessage;

public class CodeNotInAddressException extends ValidationException {

    public CodeNotInAddressException() {
        super(ErrorMessage.CODE_NOT_IN_ADDRESS);
    }
}
