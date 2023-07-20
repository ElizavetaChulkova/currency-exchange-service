package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.ErrorMessage;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.service.CurrencyService;
import ru.currency.exchange.chulkova.util.CurrencyUtils;
import ru.currency.exchange.chulkova.util.InputStringUtils;
import ru.currency.exchange.chulkova.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.*;
import static ru.currency.exchange.chulkova.exceptions.ExceptionHandler.handleException;

@WebServlet("/currency/*")
@Slf4j
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService service = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String code = InputStringUtils.parsePathInfo(req);
        log.info("GET /currency/{}", code);
        if (code.isEmpty()) {
            log.error(CODE_NOT_IN_ADDRESS.getMessage());
            handleException(resp, CODE_NOT_IN_ADDRESS);
            return;
        } else if (code.length() != 3){
            log.error(ErrorMessage.DATA_IS_INVALID.getMessage());
            handleException(resp, DATA_IS_INVALID);
            return;
        }
        try {
            log.info("get currency");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.writeJson(service.getByCode(code)));
        } catch (NotFoundException e) {
            log.error(CURRENCY_NOT_FOUND.getMessage());
            handleException(resp, CURRENCY_NOT_FOUND);
        }
    }
}
