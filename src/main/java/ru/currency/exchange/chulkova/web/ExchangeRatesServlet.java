package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.service.ExchangeRateService;
import ru.currency.exchange.chulkova.to.ExchangeRateDto;
import ru.currency.exchange.chulkova.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;
import static ru.currency.exchange.chulkova.service.ExchangeRateService.getTo;

@WebServlet("/exchangeRates")
@Slf4j
public class ExchangeRatesServlet extends BaseServlet {

    private final ExchangeRateService exchangeService = new ExchangeRateService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("GET /exchangeRates");
        List<ExchangeRate> rates = exchangeService.getAll();
        List<ExchangeRateDto> toRates = rates.stream().map(ExchangeRateService::getTo).toList();
        sendJsonResponse(resp, toRates, HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String base = req.getParameter("base");
        String target = req.getParameter("target");
        String rate = req.getParameter("rate");
        log.info("POST /exchangeRates base = {}, target = {}, rate = {}", base, target, rate);
        try {
            ValidationUtil.validateExchangeRate(base, target, rate);
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setBaseCode(base);
            exchangeRate.setTargetCode(target);
            exchangeRate.setRate(Double.parseDouble(rate));
            exchangeService.create(exchangeRate);
            log.info("created");
            sendJsonResponse(resp, getTo(exchangeRate), HttpServletResponse.SC_CREATED);
        } catch (ApplicationException e) {
            handleException(resp, e);
        }
    }
}
