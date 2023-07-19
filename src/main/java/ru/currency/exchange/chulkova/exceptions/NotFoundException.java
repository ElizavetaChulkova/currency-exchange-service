package ru.currency.exchange.chulkova.exceptions;

public class NotFoundException extends RuntimeException {

    private String msgCode;

    public NotFoundException(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsgCode() {
        return msgCode;
    }
}
