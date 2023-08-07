package ru.currency.exchange.chulkova.service;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.exists.CurrencyAlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.notfound.CurrencyNotFoundException;
import ru.currency.exchange.chulkova.model.Currency;
import ru.currency.exchange.chulkova.repository.CurrencyJdbcRepository;

import java.util.List;

@Slf4j
public class CurrencyService {
    private final CurrencyJdbcRepository currencyRepo = new CurrencyJdbcRepository();

    public List<Currency> getAll() {
        log.info("getAll currencies");
        return currencyRepo.getAll();
    }

    public Currency getByCode(String code) {
        log.info("getByCode currency : " + code);
        if (currencyRepo.getByCode(code).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyNotFoundException();
        }
        return currencyRepo.getByCode(code).get();
    }

    public Currency getById(int id) {
        log.info("getById currency : " + id);
        if (currencyRepo.getById(id).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyNotFoundException();
        }
        return currencyRepo.getById(id).get();
    }

    public Currency create(Currency currency) {
        log.info("create currency in database : " + currency.getId());
        Currency created;
        try {
            created = currencyRepo.create(currency);
        } catch (RuntimeException e) {
            log.error("already exists exception thrown");
            throw new CurrencyAlreadyExistsException();
        }
        return created;
    }

    public Currency update(Currency currency) {
        log.info("update currency in database : " + currency.getId());
        if (currencyRepo.getById(currency.getId()).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyNotFoundException();
        }
        return currencyRepo.update(currency);
    }

    public void delete(int id) {
        log.info("delete currency from database : " + id);
        if (currencyRepo.getById(id).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyNotFoundException();
        }
        currencyRepo.delete(id);
    }
}
