package ru.currency.exchange.chulkova.web;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.model.CurrencyModel;
import ru.currency.exchange.chulkova.service.CurrencyService;
import ru.currency.exchange.chulkova.util.CurrencyUtils;
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

@WebServlet("/currencies")
@Slf4j
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService service = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        log.info("GET /currencies getAll()");
        List<CurrencyModel> currencies = service.getAll();
        resp.getWriter().write(JsonUtil.writeJson(currencies));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");
        log.info("POST /currencies code = {}, name = {}, sign = {}", code, name, sign);
        if (InputStringUtils.isEmptyField(code, name, sign)) {
            log.info(EMPTY_FORM_FIELD.getMessage());
            handleException(resp, EMPTY_FORM_FIELD);
            return;
        } else if (CurrencyUtils.isNotValid(code, name, sign)) {
            log.info(DATA_IS_INVALID.getMessage());
            handleException(resp, DATA_IS_INVALID);
            return;
        }
        try {
            CurrencyModel currency = new CurrencyModel();
            currency.setCode(code);
            currency.setFullName(name);
            currency.setSign(sign);
            service.create(currency);
            log.info("created");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(JsonUtil.writeJson(currency));
        } catch (AlreadyExistsException e){
            log.info(ALREADY_EXISTS.getMessage());
            handleException(resp, ALREADY_EXISTS);
        }
    }
}
