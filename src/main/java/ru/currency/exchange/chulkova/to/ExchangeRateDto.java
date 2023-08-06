package ru.currency.exchange.chulkova.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.currency.exchange.chulkova.model.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {

    private Integer id;
    private Currency base;
    private Currency target;
    private double rate;
}
