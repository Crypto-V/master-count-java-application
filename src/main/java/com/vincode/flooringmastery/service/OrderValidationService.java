package com.vincode.flooringmastery.service;

import com.vincode.flooringmastery.dao.ProductDao;
import com.vincode.flooringmastery.dao.TaxRateDao;
import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.ProductTypeNotFoundException;
import com.vincode.flooringmastery.exceptions.StateNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderValidationService {
    private final ProductDao productDao;
    private final TaxRateDao taxDao;

    public OrderValidationService(ProductDao productDao, TaxRateDao taxDao) {
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    //validating the business requirement for the order date to be in the future.
    public String validateDate(String date) throws InvalidOrderException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate orderLocalDate = LocalDate.parse(date, formatter);
        LocalDate today = LocalDate.now();
        if (orderLocalDate.isBefore(today)) {
            throw new InvalidOrderException("Order date must be in the future: " + date);
        }
        return date.replaceAll("-","");
    }

    public void validateName(String name) throws InvalidOrderException {
        if (!name.matches("^[a-zA-Z ]+$")) {
            throw new InvalidOrderException("Customer name is invalid. Use upper or lower case letters. Space is also allowed.");
        }
    }

    public void validateState(String state) throws InvalidOrderException {
        try {
            taxDao.getRate(state);
        } catch (StateNotFoundException e) {
            throw new InvalidOrderException("State " + state + " is not supported.");
        }
    }

    public void validateArea(BigDecimal area) throws InvalidOrderException {
        BigDecimal minArea = new BigDecimal("100");
        if (area.compareTo(minArea) < 0) {
            throw new InvalidOrderException("Order area must be at least " + minArea + " square feet.");
        }
    }

    public void validateProductType(String productType) throws InvalidOrderException {
        List<BigDecimal> costs = productDao.getProductCosts(productType);
        if (costs == null) {
            throw new ProductTypeNotFoundException("Product type not found. It might be available in the future!");
        }
    }

}
