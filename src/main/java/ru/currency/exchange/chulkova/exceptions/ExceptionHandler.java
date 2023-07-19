package ru.currency.exchange.chulkova.exceptions;

import ru.currency.exchange.chulkova.util.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandler {
    public static void handleException(HttpServletResponse resp, ErrorMessage error)
            throws IOException {
        resp.setStatus(error.getStatus());
        resp.getWriter().write(JsonUtil.writeJson(new ExceptionInfo(error.getMessage())));
    }
}
