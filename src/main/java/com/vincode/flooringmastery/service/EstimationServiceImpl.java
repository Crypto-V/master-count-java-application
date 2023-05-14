package com.vincode.flooringmastery.service;

import com.vincode.flooringmastery.dao.interfaces.ProductDao;
import com.vincode.flooringmastery.dao.interfaces.TaxRateDao;
import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.ProductTypeNotFoundException;
import com.vincode.flooringmastery.exceptions.StateNotFoundException;
import com.vincode.flooringmastery.model.Order;
import com.vincode.flooringmastery.service.interfaces.EstimationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class EstimationServiceImpl implements EstimationService {

    private final ProductDao productDao;
    private final TaxRateDao taxDao;

    public EstimationServiceImpl(ProductDao productDao, TaxRateDao taxDao) {
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    public Order calculateEstimates(String name, String state, BigDecimal area, String productType) throws InvalidOrderException {
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
        } catch (StateNotFoundException | ProductTypeNotFoundException e) {
            throw new InvalidOrderException(e.getMessage());
        }
    }

}
