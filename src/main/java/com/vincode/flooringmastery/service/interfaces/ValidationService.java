package com.vincode.flooringmastery.service.interfaces;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;

import java.math.BigDecimal;


public interface ValidationService {

    /**
     * Validates the format and date should be in the future requirement.
     *
     * @param date The date to be validated.
     * @throws InvalidOrderException If the date is in an invalid format or not a valid date.
     */
    void validateDate(String date) throws InvalidOrderException;

    /**
     * Validates the format and validity of a customer name.
     *
     * @param name The customer name to be validated.
     * @throws InvalidOrderException If the customer name is empty or contains invalid characters.
     */
    void validateName(String name) throws InvalidOrderException;

    /**
     * Validates the format and validity of a state.
     *
     * @param state The state to be validated.
     * @throws InvalidOrderException If the state is non-existent.
     */
    void validateState(String state) throws InvalidOrderException;

    /**
     * Validates the format and validity of an area( Area > 100).
     *
     * @param area The area to be validated.
     * @throws InvalidOrderException If the area is less than 100.
     */
    void validateArea(BigDecimal area) throws InvalidOrderException;

    /**
     * Checks the validity of a product type.
     *
     * @param productType The product type to be validated.
     * @throws InvalidOrderException If there is no product type with specified name which is a key in an actual map.
     */
    void validateProductType(String productType) throws InvalidOrderException;
}
