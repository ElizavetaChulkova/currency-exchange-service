package ru.currency.exchange.chulkova.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.repository.ExchangeRateJdbcRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ExchangeRateService {
    private ExchangeRateJdbcRepository exchangeRepo = new ExchangeRateJdbcRepository();

    public List<ExchangeRate> getAll() {
        log.info("getAll exchange rates");
        return exchangeRepo.getAll();
    }

    public ExchangeRate getByCodePair(String baseCurrency, String targetCurrency) {
        log.info("getByCodePair exchange rate : " + baseCurrency + " " + targetCurrency);
        ExchangeRate rate = exchangeRepo.getByCodePair(baseCurrency, targetCurrency);
        if (rate.getId() == null) {
            throw new NotFoundException("Pair with these codes doesn't exist");
        }
        return exchangeRepo.getByCodePair(baseCurrency, targetCurrency);
    }

    public Optional<ExchangeRate> getById(int id) {
        log.info("getById exchange rate : " + id);
        if (exchangeRepo.getById(id).get().getId() == null) {
            throw new NotFoundException("Data with this id not found in the database");
        }
        return exchangeRepo.getById(id);
    }

    public ExchangeRate create(ExchangeRate rate) {
        log.info("create exchange rate in database : " + rate.getId());
        if (exchangeRepo.getByCodePair(rate.getBase().getCode(), rate.getTarget().getCode()).getId() != null){
            throw new AlreadyExistsException("Currency with such code already exists");
        }
        return exchangeRepo.create(rate);
    }

    public ExchangeRate update(ExchangeRate rate) {
        log.info("update exchange rate in database : " + rate.getId());
        if (rate.getId() == null || exchangeRepo.getById(rate.getId()).get().getId() == null) {
            throw new NotFoundException("Data with this id not found in the database");
        }
        return exchangeRepo.update(rate);
    }

    public void delete(int id) {
        log.info("delete exchange rate from database : " + id);
        if (exchangeRepo.getById(id).get().getId() == null) {
            throw new NotFoundException("Data with this id not found in the database");
        }
        exchangeRepo.delete(id);
    }
}
