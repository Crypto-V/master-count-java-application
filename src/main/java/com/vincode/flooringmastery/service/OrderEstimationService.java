package com.vincode.flooringmastery.service;

import com.vincode.flooringmastery.dao.ProductDao;
import com.vincode.flooringmastery.dao.TaxRateDao;
import com.vincode.flooringmastery.exceptions.ProductTypeNotFoundException;
import com.vincode.flooringmastery.exceptions.StateNotFoundException;
import com.vincode.flooringmastery.model.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OrderEstimationService {

    private final ProductDao productDao;
    private final TaxRateDao taxDao;

    public OrderEstimationService(ProductDao productDao, TaxRateDao taxDao) {
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    public Order calculateEstimates(String name, String state, BigDecimal area, String productType) {
        try {
            Order order = new Order();
            List<BigDecimal> productCosts = productDao.getProductCosts(productType);
            BigDecimal taxRate = taxDao.getRate(state);
            order.setState(state);
            order.setProductType(productType);
            order.setCustomerName(name);
            order.setCostPerSquareFoot(productCosts.get(0));
            order.setLaborCostPerSquareFoot(productCosts.get(1));
            order.setTaxRate(taxRate);
            order.setArea(area);

            BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot());
            order.setMaterialCost(materialCost);
            BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot());
            order.setLaborCost(laborCost);
            BigDecimal tax = materialCost.add(laborCost).multiply(order.getTaxRate().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            order.setTax(tax);
            BigDecimal total = materialCost.add(laborCost).add(tax);
            order.setTotal(total);

            return order;
        } catch (StateNotFoundException  | ProductTypeNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
