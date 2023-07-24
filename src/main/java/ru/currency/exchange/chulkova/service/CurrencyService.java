package ru.currency.exchange.chulkova.service;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.CurrencyModel;
import ru.currency.exchange.chulkova.repository.CurrencyJdbcRepository;

import java.util.List;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.ALREADY_EXISTS;
import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.CURRENCY_NOT_FOUND;

@Slf4j
public class CurrencyService {
    private final CurrencyJdbcRepository currencyRepo = new CurrencyJdbcRepository();

    public List<CurrencyModel> getAll() {
        log.info("getAll currencies");
        return currencyRepo.getAll();
    }

    public CurrencyModel getByCode(String code) {
        log.info("getByCode currency : " + code);
        CurrencyModel currency = currencyRepo.getByCode(code).get();
        if (currency.getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(CURRENCY_NOT_FOUND);
        }
        return currency;
    }

    public CurrencyModel getById(int id) {
        log.info("getById currency : " + id);
        CurrencyModel currency = currencyRepo.getById(id).orElseThrow();
        if (currency.getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(CURRENCY_NOT_FOUND);
        }
        return currencyRepo.getById(id).orElseThrow();
    }

    public CurrencyModel create(CurrencyModel currency) {
        log.info("create currency in database : " + currency.getId());
        if (currencyRepo.getByCode(currency.getCode()).get().getId() != null) {
            log.error("already exists exception thrown");
            throw new AlreadyExistsException(ALREADY_EXISTS);
        }
        return currencyRepo.create(currency);
    }

    public CurrencyModel update(CurrencyModel currency) {
        log.info("update currency in database : " + currency.getId());
        if (currency.getId() == null || currencyRepo.getById(currency.getId()).get().getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(CURRENCY_NOT_FOUND);
        }
        return currencyRepo.update(currency);
    }

    public void delete(int id) {
        log.info("delete currency from database : " + id);
        if (currencyRepo.getById(id).isEmpty() || currencyRepo.getById(id).get().getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(CURRENCY_NOT_FOUND);
        }
        currencyRepo.delete(id);
    }
}
