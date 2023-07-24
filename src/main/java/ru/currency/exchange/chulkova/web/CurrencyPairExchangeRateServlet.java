package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.exceptions.ValidationException;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.service.ExchangeRateService;
import ru.currency.exchange.chulkova.util.InputStringUtils;
import ru.currency.exchange.chulkova.util.JsonUtil;
import ru.currency.exchange.chulkova.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.DATA_IS_INVALID;
import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;

@WebServlet("/exchangeRate/*")
@Slf4j
public class CurrencyPairExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService service = new ExchangeRateService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pair = InputStringUtils.parsePathInfo(req);
        log.info("GET /exchangeRate/{}", pair);
        try {
            ValidationUtil.validateExchangeRate(pair);
            String base = pair.substring(0, 3);
            String target = pair.substring(3);
            ExchangeRate rate = service.getByCodePair(base, target);
            log.info("get exchange rate {} - {}", base, target);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.writeJson(rate));
        } catch (ApplicationException e) {
            handleException(resp, e);
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        String pair = InputStringUtils.parsePathInfo(req);
        String rate = req.getParameter("rate");
        log.info("PUT /exchangeRate/{}", pair);
        try {
            ValidationUtil.validateExchangeRate(pair);
            if (rate == null) {
                throw new ValidationException(DATA_IS_INVALID);
            }
            String base = pair.substring(0, 3);
            String target = pair.substring(3);
            ExchangeRate toUpdate = service.getByCodePair(base, target);
            toUpdate.setRate(Double.parseDouble(rate));
            service.update(toUpdate);
            log.info("updated");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.writeJson(toUpdate));
        } catch (ApplicationException e) {
            handleException(resp, e);
        }
    }
}
