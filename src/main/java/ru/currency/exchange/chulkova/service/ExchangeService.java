package ru.currency.exchange.chulkova.service;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.repository.CurrencyJdbcRepository;
import ru.currency.exchange.chulkova.repository.ExchangeRateJdbcRepository;
import ru.currency.exchange.chulkova.to.ExchangeDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class ExchangeService {
    private static final ExchangeRateService exchangeService = new ExchangeRateService();
    private static final ExchangeRateJdbcRepository exRepo = new ExchangeRateJdbcRepository();
    private static final CurrencyService currencyService = new CurrencyService();
    private static final CurrencyJdbcRepository currencyRepo = new CurrencyJdbcRepository();

    private static final int ROUND_SCALE = 4;

    public double exchange(String from, String to, String amountString) {
        double answer = 0.0;
        double amount = Double.parseDouble(amountString);
        log.debug("exchange from {} to {} amount {}", from, to, amount);
        if (currencyRepo.getByCode(from).getId() == null || currencyRepo.getByCode(to).getId() == null ||
                from.equals(to)) {
            throw new NotFoundException("This exchange rate doesn't exist");
        } else if (exRepo.getByCodePair(from, to).getId() != null) {
            log.info("straight strategy from {} to {}", from, to);
            ExchangeRate changePair = exchangeService.getByCodePair(from, to);
            double rate = changePair.getRate();
            answer = roundDoubles(amount * rate);
        } else if (exRepo.getByCodePair(to, from).getId() != null) {
            log.info("reversed strategy to {} from {}", to, from);
            ExchangeRate changePair = exchangeService.getByCodePair(to, from);
            double rate = 1 / changePair.getRate();
            answer = roundDoubles(amount * rate);
        } else if ((exRepo.getByCodePair("USD", from) != null) &&
                (exRepo.getByCodePair("USD", to) != null)) {
            log.info("USD cross-rate strategy USD - {}, USD - {}", from, to);
            String base = "USD";
            ExchangeRate usdFromRate = exchangeService.getByCodePair(base, from);
            ExchangeRate usdToRate = exchangeService.getByCodePair(base, to);
            double rate = 1 / (usdFromRate.getRate() / usdToRate.getRate());
            answer = roundDoubles(amount * rate);
        }
        return answer;
    }

    public ExchangeDTO getTo(String from, String to, String amount) {
        ExchangeDTO dto = new ExchangeDTO();
        dto.setBase(currencyService.getByCode(from));
        dto.setTarget(currencyService.getByCode(to));
        dto.setAmount(Double.parseDouble(amount));
        dto.setRate(exchangeService.getByCodePair(from, to).getRate());
        dto.setConvertedAmount(exchange(from, to, amount));
        log.info("get transfer object");
        return dto;
    }

    private static double roundDoubles(double number) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(ROUND_SCALE, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
