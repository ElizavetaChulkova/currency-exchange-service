package ru.currency.exchange.chulkova.service;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.repository.CurrencyJdbcRepository;
import ru.currency.exchange.chulkova.repository.ExchangeRateJdbcRepository;
import ru.currency.exchange.chulkova.to.ExchangeRateDto;

import java.util.List;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.*;

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
        try {
            if (exchangeRepo.getByCodePair(baseCurrency, targetCurrency).get().getId() == null) {
                log.error("not found exception thrown");
                throw new NotFoundException(PAIR_NOT_FOUND);
            }
            return exchangeRepo.getByCodePair(baseCurrency, targetCurrency).get();
        } catch (NullPointerException e) {
            log.error("null pointer exception thrown");
            throw new NotFoundException(PAIR_NOT_FOUND);
        }
    }

    public ExchangeRateDto getById(int id) {
        log.info("getById exchange rate : " + id);
        if (exchangeRepo.getById(id).get().getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(PAIR_EXCHANGE_RATE_NOT_FOUND);
        }
        return getTo(exchangeRepo.getById(id).get());
    }

    public ExchangeRateDto create(ExchangeRate rate) {
        log.info("create exchange rate in database : " + rate.getId());
        ExchangeRateDto to = getTo(rate);
        if (exchangeRepo.getByCodePair(to.getBase().getCode(), to.getTarget().getCode()).get().getId() != null) {
            log.error("already exists exception thrown");
            throw new AlreadyExistsException(PAIR_ALREADY_EXISTS);
        }
        return getTo(exchangeRepo.create(rate));
    }

    public ExchangeRateDto update(ExchangeRate rate) {
        log.info("update exchange rate in database : " + rate.getId());
        if (rate.getId() == null || exchangeRepo.getById(rate.getId()).get().getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(PAIR_EXCHANGE_RATE_NOT_FOUND);
        }
        return getTo(exchangeRepo.update(rate));
    }

    public void delete(int id) {
        log.info("delete exchange rate from database : " + id);
        if (exchangeRepo.getById(id).get().getId() == null) {
            log.error("not found exception thrown");
            throw new NotFoundException(PAIR_EXCHANGE_RATE_NOT_FOUND);
        }
        exchangeRepo.delete(id);
    }

    public static ExchangeRateDto getTo(ExchangeRate rate) {
        ExchangeRateDto to = new ExchangeRateDto();
        to.setId(rate.getId());
        to.setBase(currencyRepo.getById(rate.getBase()).get());
        to.setTarget(currencyRepo.getById(rate.getTarget()).get());
        to.setRate(rate.getRate());
        return to;
    }
}
