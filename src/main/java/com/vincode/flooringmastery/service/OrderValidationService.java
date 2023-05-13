package com.vincode.flooringmastery.service;

import com.vincode.flooringmastery.dao.interfaces.ProductDao;
import com.vincode.flooringmastery.dao.interfaces.TaxRateDao;
import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.ProductTypeNotFoundException;
import com.vincode.flooringmastery.exceptions.StateNotFoundException;
import com.vincode.flooringmastery.service.interfaces.ValidationManagementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class OrderValidationService implements ValidationManagementService {
    private final ProductDao productDao;
    private final TaxRateDao taxDao;

    public OrderValidationService(ProductDao productDao, TaxRateDao taxDao) {
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    // Validating the business requirement for the order date to be in the future.
    public void validateDate(String date) throws InvalidOrderException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate orderLocalDate = LocalDate.parse(date, formatter);
        LocalDate today = LocalDate.now();
        if (orderLocalDate.isBefore(today)) {
            throw new InvalidOrderException("--! Order date must be in the future: " + date);
        }
    }

    public void validateName(String name) throws InvalidOrderException {
        if (!name.matches("^[a-zA-Z0-9., ]+$")) {
            throw new InvalidOrderException("--! Name can't be blank, limited to characters [a-z][0-9] as well as periods and comma characters.");
        }
    }

    public void validateState(String state) throws InvalidOrderException {
        try {
            taxDao.getRate(state);
        } catch (StateNotFoundException e) {
            throw new InvalidOrderException("--! State " + state + " is not supported.");
        }
    }

    public void validateArea(BigDecimal area) throws InvalidOrderException {
        BigDecimal minArea = new BigDecimal("100");
        if (area.compareTo(minArea) < 0) {
            throw new InvalidOrderException("--! Order area must be at least " + minArea + " square feet.");
        }
    }

    public void validateProductType(String productType) throws InvalidOrderException {
        try{
            productDao.getProductCosts(productType);
        }catch (ProductTypeNotFoundException e){
            throw new InvalidOrderException("--! Product type:  " + productType + " is not supported.");
        }
    }
}
