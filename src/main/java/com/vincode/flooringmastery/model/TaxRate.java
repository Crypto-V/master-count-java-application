package com.vincode.flooringmastery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class TaxRate {

    private String state;
    private String stateName;
    private BigDecimal taxRate;
}
