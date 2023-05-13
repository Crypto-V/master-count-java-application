package com.vincode.flooringmastery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "\nOrder Details:\n" +
                "Order Number: " + orderNumber + "\n" +
                "Customer Name: " + customerName + "\n" +
                "State: " + state + "\n" +
                "Tax Rate: " + taxRate + "%\n" +
                "Product Type: " + productType + "\n" +
                "Area: " + area + " sq.Ft\n" +
                "Cost Per Square Foot: $" + costPerSquareFoot + "\n" +
                "Labor Cost Per Square Foot: $" + laborCostPerSquareFoot + "\n" +
                "Material Cost: $" + materialCost + "\n" +
                "Labor Cost: $" + laborCost + "\n" +
                "Tax: $" + tax + "\n" +
                "Total: $" + total;
    }

    public String toExportString() {
        return orderNumber + "," +
                customerName + "," +
                state + "," +
                taxRate + "," +
                productType + "," +
                area + "," +
                costPerSquareFoot + "," +
                laborCostPerSquareFoot + "," +
                materialCost + "," +
                laborCost + "," +
                tax + "," +
                total;
    }


}
