package ru.currency.exchange.chulkova.service;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.exists.CurrencyPairAlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.notfound.CurrencyNotFoundException;
import ru.currency.exchange.chulkova.exceptions.notfound.CurrencyPairExchangeRateNotFoundException;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.repository.CurrencyJdbcRepository;
import ru.currency.exchange.chulkova.repository.ExchangeRateJdbcRepository;
import ru.currency.exchange.chulkova.to.ExchangeRateDto;

import java.util.List;

@Slf4j
public class ExchangeRateService {
    private final ExchangeRateJdbcRepository exchangeRepo = new ExchangeRateJdbcRepository();
    private static final CurrencyJdbcRepository currencyRepo = new CurrencyJdbcRepository();

    public List<ExchangeRate> getAll() {
        log.info("getAll exchange rates");
        return exchangeRepo.getAll();
    }

    public ExchangeRate getByCodePair(String baseCurrency, String targetCurrency) {
        log.info("getByCodePair exchange rate : " + baseCurrency + " " + targetCurrency);
        if (currencyRepo.getByCode(baseCurrency).isEmpty() || currencyRepo.getByCode(targetCurrency).isEmpty()) {
            log.error("currency not found");
            throw new CurrencyNotFoundException();
        } else if (exchangeRepo.getByCodePair(baseCurrency, targetCurrency).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyPairExchangeRateNotFoundException();
        }
        return exchangeRepo.getByCodePair(baseCurrency, targetCurrency).get();
    }

    public ExchangeRate getById(int id) {
        log.info("getById exchange rate : " + id);
        if (exchangeRepo.getById(id).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyPairExchangeRateNotFoundException();
        }
        return exchangeRepo.getById(id).get();
    }

    public ExchangeRate create(ExchangeRate rate) {
        log.info("create exchange rate in database : " + rate.getId());
        ExchangeRate created;
        try {
            created = exchangeRepo.create(rate);
        } catch (RuntimeException e) {
            log.error("already exists exception thrown");
            throw new CurrencyPairAlreadyExistsException();
        }
        return created;
    }

    public ExchangeRate update(ExchangeRate rate) {
        log.info("update exchange rate in database : " + rate.getId());
        if (exchangeRepo.getById(rate.getId()).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyPairExchangeRateNotFoundException();
        }
        return exchangeRepo.update(rate);
    }

    public void delete(int id) {
        log.info("delete exchange rate from database : " + id);
        if (exchangeRepo.getById(id).isEmpty()) {
            log.error("not found exception thrown");
            throw new CurrencyPairExchangeRateNotFoundException();
        }
        exchangeRepo.delete(id);
    }

    public static ExchangeRateDto getTo(ExchangeRate rate) {
        ExchangeRateDto to = new ExchangeRateDto();
        to.setId(rate.getId());
        to.setBase(currencyRepo.getByCode(rate.getBaseCode()).get());
        to.setTarget(currencyRepo.getByCode(rate.getTargetCode()).get());
        to.setRate(rate.getRate());
        return to;
    }
}
