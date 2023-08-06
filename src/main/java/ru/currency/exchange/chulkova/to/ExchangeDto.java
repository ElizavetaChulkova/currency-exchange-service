package ru.currency.exchange.chulkova.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.currency.exchange.chulkova.model.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDto {

    private Currency base;
    private Currency target;
    private double rate;
    private double amount;
    private double convertedAmount;
}
