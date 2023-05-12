package com.vincode.flooringmastery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Order {

    //Order number will be incremented in the constructor
//    private String orderDate;
    private static Integer nextNumber = 1;
    private Integer orderNumber;
    private String customerName;
    private String state;
    private BigDecimal taxRate;
    private String productType ;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost; // MaterialCost = (Area * CostPerSquareFoot)
    private BigDecimal laborCost; //LaborCost = (Area * LaborCostPerSquareFoot)
    private BigDecimal tax; // Tax = (MaterialCost + LaborCost) * (TaxRate/100)
    private BigDecimal total; // Total = (MaterialCost + LaborCost + Tax)

    public Order() {
        this.orderNumber = nextNumber;
    }

    //This constructor will be initialized when user will provide the initial data.
    public Order( String state, BigDecimal area, String productType) {
        this.state = state;
        this.area = area;
        this.productType = productType;
        this.orderNumber = nextNumber;
//        this.orderDate = orderDate;
    }

    public Order(
            String customerName,
            String state,
            BigDecimal taxRate,
            String productType,
            BigDecimal area,
            BigDecimal costPerSquareFoot,
            BigDecimal laborCostPerSquareFoot,
            BigDecimal materialCost,
            BigDecimal laborCost,
            BigDecimal tax,
            BigDecimal total) {
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
        this.orderNumber = nextNumber;
        nextNumber++;
    }

}
