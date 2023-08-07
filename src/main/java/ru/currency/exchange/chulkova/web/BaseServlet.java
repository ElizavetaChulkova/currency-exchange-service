package ru.currency.exchange.chulkova.web;

import ru.currency.exchange.chulkova.util.JsonUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseServlet extends HttpServlet {

    <T> void sendJsonResponse(HttpServletResponse resp, T payload, Integer status) throws IOException {
        resp.getWriter().write(JsonUtil.writeJson(payload));
        resp.setStatus(status);
    }
}
