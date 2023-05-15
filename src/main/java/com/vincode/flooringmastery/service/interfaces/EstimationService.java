package com.vincode.flooringmastery.service.interfaces;


import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.model.Order;
import java.math.BigDecimal;


public interface EstimationService {

    /**
     * Calculates the cost estimates for an order based on the provided parameters.
     *
     * @param name  The customer name for the order.
     * @param state The state where the order is placed.
     * @param area  The area (in square feet) for the order.
     * @param productType The type of product for the order.
     *
     * @return An Order object representing the calculated estimates.
     * @throws InvalidOrderException If state or product type will throw their exceptions.
     */
    Order calculateEstimates(String name, String state, BigDecimal area, String productType) throws InvalidOrderException;
}
