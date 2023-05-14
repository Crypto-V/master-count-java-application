package com.vincode.flooringmastery.service.interfaces;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;

import java.math.BigDecimal;


public interface ValidationService {

    void validateDate(String date) throws InvalidOrderException;

    void validateName(String name) throws InvalidOrderException;

    void validateState(String state) throws InvalidOrderException;

    void validateArea(BigDecimal area) throws InvalidOrderException;

    void validateProductType(String productType) throws InvalidOrderException;
}
