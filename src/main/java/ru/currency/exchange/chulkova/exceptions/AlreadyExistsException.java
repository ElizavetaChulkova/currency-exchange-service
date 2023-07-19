package ru.currency.exchange.chulkova.exceptions;

public class AlreadyExistsException extends RuntimeException {
    private String msgCode;

    public AlreadyExistsException(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsgCode() {
        return msgCode;
    }
}
