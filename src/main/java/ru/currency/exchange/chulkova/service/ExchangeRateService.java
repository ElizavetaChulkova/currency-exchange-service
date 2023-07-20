package ru.currency.exchange.chulkova.service;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.ErrorMessage;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.repository.ExchangeRateJdbcRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ExchangeRateService {
    private final ExchangeRateJdbcRepository exchangeRepo = new ExchangeRateJdbcRepository();

    public List<ExchangeRate> getAll() {
        log.info("getAll exchange rates");
        return exchangeRepo.getAll();
    }

    public ExchangeRate getByCodePair(String baseCurrency, String targetCurrency) {
        log.info("getByCodePair exchange rate : " + baseCurrency + " " + targetCurrency);
        try {
            if (exchangeRepo.getByCodePair(baseCurrency, targetCurrency).get().getId() == null) {
                log.error("not found exception thrown");
                throw new NotFoundException(ErrorMessage.PAIR_NOT_FOUND.getMessage());
            }
            return exchangeRepo.getByCodePair(baseCurrency, targetCurrency).get();
        } catch (NullPointerException e) {
            log.error("null pointer exception thrown");
            throw new NotFoundException(ErrorMessage.PAIR_NOT_FOUND.getMessage());
        }
    }

    public Optional<ExchangeRate> getById(int id) {
        log.info("getById exchange rate : " + id);
        if (exchangeRepo.getById(id).get().getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(ErrorMessage.PAIR_EXCHANGE_RATE_NOT_FOUND.getMessage());
        }
        return exchangeRepo.getById(id);
    }

    public ExchangeRate create(ExchangeRate rate) {
        log.info("create exchange rate in database : " + rate.getId());
        if (exchangeRepo.getByCodePair(rate.getBase().getCode(), rate.getTarget().getCode()).get().getId() != null) {
            log.error("already exists exception thrown");
            throw new AlreadyExistsException(ErrorMessage.PAIR_ALREADY_EXISTS.getMessage());
        }
        return exchangeRepo.create(rate);
    }

    public ExchangeRate update(ExchangeRate rate) {
        log.info("update exchange rate in database : " + rate.getId());
        if (rate.getId() == null || exchangeRepo.getById(rate.getId()).get().getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(ErrorMessage.PAIR_EXCHANGE_RATE_NOT_FOUND.getMessage());
        }
        return exchangeRepo.update(rate);
    }

    public void delete(int id) {
        log.info("delete exchange rate from database : " + id);
        if (exchangeRepo.getById(id).get().getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(ErrorMessage.PAIR_EXCHANGE_RATE_NOT_FOUND.getMessage());
        }
        exchangeRepo.delete(id);
    }
}
