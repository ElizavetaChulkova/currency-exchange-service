package ru.currency.exchange.chulkova.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    private Integer id;
    private CurrencyModel base;
    private CurrencyModel target;
    private double rate;
}
