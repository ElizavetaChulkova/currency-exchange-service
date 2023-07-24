package ru.currency.exchange.chulkova.exceptions;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.util.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionHandler {

    public static void handleException(HttpServletResponse resp, ErrorMessage error)
            throws IOException {
        resp.setStatus(error.getStatus());
        resp.getWriter().write(JsonUtil.writeJson(new ExceptionInfo(error.getMessage())));
    }

    public static void handleException(HttpServletResponse resp, ApplicationException e) throws IOException {
        log.error(e.getError().getMessage());
        resp.setStatus(e.getError().getStatus());
        resp.getWriter().write(JsonUtil.writeJson(new ExceptionInfo(e.getError().getMessage())));
    }
}
