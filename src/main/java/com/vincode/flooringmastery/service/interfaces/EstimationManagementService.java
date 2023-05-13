package com.vincode.flooringmastery.service.interfaces;


import com.vincode.flooringmastery.model.Order;
import java.math.BigDecimal;


public interface EstimationManagementService {

    public Order calculateEstimates(String name, String state, BigDecimal area, String productType);
}
