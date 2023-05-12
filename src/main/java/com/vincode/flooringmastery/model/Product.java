package com.vincode.flooringmastery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;

}
