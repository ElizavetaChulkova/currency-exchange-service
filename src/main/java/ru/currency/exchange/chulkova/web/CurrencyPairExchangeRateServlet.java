package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.service.CurrencyService;
import ru.currency.exchange.chulkova.service.ExchangeRateService;
import ru.currency.exchange.chulkova.util.ExchangeRateUtils;
import ru.currency.exchange.chulkova.util.InputStringUtils;
import ru.currency.exchange.chulkova.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.*;
import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;

@WebServlet("/exchangeRate/*")
@Slf4j
public class CurrencyPairExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService service = new ExchangeRateService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String pair = InputStringUtils.parsePathInfo(req);
        log.info("GET /exchangeRate/{}", pair);
        if (pair.isEmpty()) {
            log.error(CODE_NOT_IN_ADDRESS.getMessage());
            handleException(resp, CODE_NOT_IN_ADDRESS);
        } else if (!ExchangeRateUtils.isCorrectPair(pair)) {
            log.error(DATA_IS_INVALID.getMessage());
            handleException(resp, DATA_IS_INVALID);
            return;
        }
        String base = pair.substring(0, 3);
        String target = pair.substring(3);
        try {
            ExchangeRate rate = service.getByCodePair(base, target);
            log.info("get exchange rate {} - {}", base, target);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.writeJson(rate));
        } catch (NotFoundException e) {
            log.error(PAIR_EXCHANGE_RATE_NOT_FOUND.getMessage());
            handleException(resp, PAIR_EXCHANGE_RATE_NOT_FOUND);
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String pair = InputStringUtils.parsePathInfo(req);
        String rate = req.getParameter("rate");
        log.info("PUT /exchangeRate/{}", pair);
        if (pair.isEmpty()) {
            log.error(CODE_NOT_IN_ADDRESS.getMessage());
            handleException(resp, CODE_NOT_IN_ADDRESS);
            return;
        } else if (!ExchangeRateUtils.isCorrectPair(pair) || rate == null) {
            log.error(DATA_IS_INVALID.getMessage());
            handleException(resp, DATA_IS_INVALID);
            return;
        }
        String base = pair.substring(0, 3);
        String target = pair.substring(3);
        try {
            ExchangeRate toUpdate = service.getByCodePair(base, target);
            toUpdate.setRate(Double.parseDouble(rate));
            service.update(toUpdate);
            log.info("updated");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.writeJson(toUpdate));
        } catch (NotFoundException e) {
            log.error(PAIR_EXCHANGE_RATE_NOT_FOUND.getMessage());
            handleException(resp, PAIR_EXCHANGE_RATE_NOT_FOUND);
        }
    }
}
