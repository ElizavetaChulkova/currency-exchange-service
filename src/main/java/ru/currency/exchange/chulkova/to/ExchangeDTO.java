package ru.currency.exchange.chulkova.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.currency.exchange.chulkova.model.CurrencyModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDTO {
    private CurrencyModel base;
    private CurrencyModel target;
    private double rate;
    private double amount;
    private double convertedAmount;
}
