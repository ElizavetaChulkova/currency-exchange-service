package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.service.ExchangeService;
import ru.currency.exchange.chulkova.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;

@WebServlet("/exchange")
@Slf4j
public class ExchangeServlet extends BaseServlet {

    private final ExchangeService service = new ExchangeService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");
        log.info("GET /exchange from = {}, to = {}, amount = {}", from, to, amount);
        try {
            ValidationUtil.validateExchange(from, to, amount);
            sendJsonResponse(resp, service.getTo(from, to, amount), HttpServletResponse.SC_OK);
            log.info("exchanged");
        } catch (ApplicationException e) {
            handleException(resp, e);
        }
    }
}
