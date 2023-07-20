package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.service.CurrencyService;
import ru.currency.exchange.chulkova.service.ExchangeRateService;
import ru.currency.exchange.chulkova.util.ExchangeRateUtils;
import ru.currency.exchange.chulkova.util.InputStringUtils;
import ru.currency.exchange.chulkova.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.*;
import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;

@WebServlet("/exchangeRates")
@Slf4j
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService exchangeService = new ExchangeRateService();
    private CurrencyService currencyService = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        log.info("GET /exchangeRates");
        List<ExchangeRate> rates = exchangeService.getAll();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(JsonUtil.writeJson(rates));
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String base = req.getParameter("base");
        String target = req.getParameter("target");
        String rate = req.getParameter("rate");
        log.info("POST /exchangeRates base = {}, target = {}, rate = {}", base, target, rate);
        if (InputStringUtils.isEmptyField(base, target, rate)) {
            log.error(EMPTY_FORM_FIELD.getMessage());
            handleException(resp, EMPTY_FORM_FIELD);
            return;
        } else if (!ExchangeRateUtils.isCorrectArgs(base, target)) {
            log.error(DATA_IS_INVALID.getMessage());
            handleException(resp, DATA_IS_INVALID);
            return;
        }
        try {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setBase(currencyService.getByCode(base));
            exchangeRate.setTarget(currencyService.getByCode(target));
            exchangeRate.setRate(Double.parseDouble(rate));
            exchangeService.create(exchangeRate);
            log.info("created");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(JsonUtil.writeJson(exchangeRate));
        } catch (AlreadyExistsException e) {
            log.error(PAIR_ALREADY_EXISTS.getMessage());
            handleException(resp, PAIR_ALREADY_EXISTS);
        }
    }
}
