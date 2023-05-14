package com.vincode.flooringmastery.service.interfaces;


import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.model.Order;
import java.math.BigDecimal;


public interface EstimationService {

    Order calculateEstimates(String name, String state, BigDecimal area, String productType) throws InvalidOrderException;
}
