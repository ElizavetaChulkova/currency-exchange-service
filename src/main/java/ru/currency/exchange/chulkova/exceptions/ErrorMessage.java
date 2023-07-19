package ru.currency.exchange.chulkova.exceptions;

import javax.servlet.http.HttpServletResponse;

public enum ErrorMessage {

    CURRENCY_NOT_FOUND("Currency not found",
            HttpServletResponse.SC_NOT_FOUND),
    CODE_NOT_IN_ADDRESS("Currency code is not in address",
            HttpServletResponse.SC_BAD_REQUEST),
    EMPTY_FORM_FIELD("Required form field is missing",
            HttpServletResponse.SC_BAD_REQUEST),
    ALREADY_EXISTS("Currency with this code already exists",
            HttpServletResponse.SC_CONFLICT),
    PAIR_EXCHANGE_RATE_NOT_FOUND("Currency pair exchange rate is not found",
            HttpServletResponse.SC_NOT_FOUND),
    PAIR_NOT_FOUND("Currency pair is not found",
            HttpServletResponse.SC_NOT_FOUND),
    PAIR_ALREADY_EXISTS("Currency pair already exists",
            HttpServletResponse.SC_CONFLICT),
    DATA_IS_INVALID("Input data is invalid", HttpServletResponse.SC_BAD_REQUEST),
    ERROR("Application error",
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

    private final String message;
    private final int status;

    ErrorMessage(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
