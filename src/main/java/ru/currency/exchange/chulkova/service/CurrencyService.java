package ru.currency.exchange.chulkova.service;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.CurrencyModel;
import ru.currency.exchange.chulkova.repository.CurrencyJdbcRepository;

import java.util.List;

@Slf4j
public class CurrencyService {
    private CurrencyJdbcRepository currencyRepo = new CurrencyJdbcRepository();

    public List<CurrencyModel> getAll() {
        log.info("getAll currencies");
        return currencyRepo.getAll();
    }

    public CurrencyModel getByCode(String code) {
        log.info("getByCode currency : " + code);
        CurrencyModel currency = currencyRepo.getByCode(code);
        if (currency.getId() == null) {
            throw new NotFoundException("Data with this code not found in the database");
        }
        return currency;
    }

    public CurrencyModel getById(int id) {
        log.info("getById currency : " + id);
        CurrencyModel currency = currencyRepo.getById(id).orElseThrow();
        if (currency.getId() == null) {
            throw new NotFoundException("Data with this id not found in the database");
        }
        return currencyRepo.getById(id).orElseThrow();
    }

    public CurrencyModel create(CurrencyModel currency) {
        log.info("create currency in database : " + currency.getId());
        if (currencyRepo.getByCode(currency.getCode()).getId() != null){
            throw new AlreadyExistsException("Currency with such code already exists");
        }
        return currencyRepo.create(currency);
    }

    public CurrencyModel update(CurrencyModel currency) {
        log.info("update currency in database : " + currency.getId());
        if (currency.getId() == null  || currencyRepo.getById(currency.getId()).get().getId() == null) {
            throw new NotFoundException("Data with this id not found in the database");
        }
        return currencyRepo.update(currency);
    }

    public void delete(int id) {
        log.info("delete currency from database : " + id);
        if (currencyRepo.getById(id).isEmpty() || currencyRepo.getById(id).get().getId() == null) {
            throw new NotFoundException("Data with this id not found in the database");
        }
        currencyRepo.delete(id);
    }
}
