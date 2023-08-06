package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.model.Currency;
import ru.currency.exchange.chulkova.service.CurrencyService;
import ru.currency.exchange.chulkova.util.JsonUtil;
import ru.currency.exchange.chulkova.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;

@WebServlet("/currencies")
@Slf4j
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService service = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        log.info("GET /currencies getAll()");
        List<Currency> currencies = service.getAll();
        resp.getWriter().write(JsonUtil.writeJson(currencies));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");
        log.info("POST /currencies code = {}, name = {}, sign = {}", code, name, sign);
        try {
            ValidationUtil.validateCurrency(code, name, sign);
            Currency currency = new Currency();
            currency.setCode(code);
            currency.setFullName(name);
            currency.setSign(sign);
            service.create(currency);
            log.info("created");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(JsonUtil.writeJson(currency));
        } catch (ApplicationException e) {
            handleException(resp, e);
        }
    }
}
