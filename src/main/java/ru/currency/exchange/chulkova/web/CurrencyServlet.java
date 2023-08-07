package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.service.CurrencyService;
import ru.currency.exchange.chulkova.util.InputStringUtils;
import ru.currency.exchange.chulkova.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;

@WebServlet("/currency/*")
@Slf4j
public class CurrencyServlet extends BaseServlet {
    private final CurrencyService service = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = InputStringUtils.parsePathInfo(req);
        log.info("GET /currency/{}", code);
        try {
            ValidationUtil.validateCurrency(code);
            log.info("get currency");
            sendJsonResponse(resp, service.getByCode(code), HttpServletResponse.SC_OK);
        } catch (ApplicationException e) {
            handleException(resp, e);
        }
    }
}
