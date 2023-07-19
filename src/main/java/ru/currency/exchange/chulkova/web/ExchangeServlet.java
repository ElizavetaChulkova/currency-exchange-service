package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.service.ExchangeService;
import ru.currency.exchange.chulkova.util.ExchangeRateUtils;
import ru.currency.exchange.chulkova.util.InputStringUtils;
import ru.currency.exchange.chulkova.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.*;
import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;

@WebServlet("/exchange")
@Slf4j
public class ExchangeServlet extends HttpServlet {

    private ExchangeService service = new ExchangeService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");
        log.info("GET /exchange from = {}, to = {}, amount = {}", from, to, amount);
        if (InputStringUtils.isEmptyField(from, to, amount)) {
            log.error(EMPTY_FORM_FIELD.getMessage());
            handleException(resp, EMPTY_FORM_FIELD);
        } else if (!ExchangeRateUtils.isCorrectArgs(from, to)) {
            log.error(DATA_IS_INVALID.getMessage());
            handleException(resp, DATA_IS_INVALID);
        } else if (!ExchangeRateUtils.isPairExisted(from, to)) {
            log.error(PAIR_EXCHANGE_RATE_NOT_FOUND.getMessage());
            handleException(resp, PAIR_EXCHANGE_RATE_NOT_FOUND);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.writeJson(service.getTo(from, to, amount)));
            log.info("exchanged");
        }
    }
}
