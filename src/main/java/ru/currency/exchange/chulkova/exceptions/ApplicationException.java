package ru.currency.exchange.chulkova.exceptions;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private ErrorMessage error;

    public ApplicationException(ErrorMessage error) {
        this.error = error;
    }
}
