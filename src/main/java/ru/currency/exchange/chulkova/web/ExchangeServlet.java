package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.service.ExchangeService;
import ru.currency.exchange.chulkova.util.JsonUtil;
import ru.currency.exchange.chulkova.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;

@WebServlet("/exchange")
@Slf4j
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService service = new ExchangeService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");
        log.info("GET /exchange from = {}, to = {}, amount = {}", from, to, amount);
        try {
            ValidationUtil.validateExchange(from, to, amount);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.writeJson(service.getTo(from, to, amount)));
            log.info("exchanged");
        } catch (ApplicationException e) {
            handleException(resp, e);
        }
    }
}
