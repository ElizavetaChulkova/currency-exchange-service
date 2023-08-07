package ru.currency.exchange.chulkova.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    private Integer id;
    private String baseCode;
    private String targetCode;
    private double rate;
}
