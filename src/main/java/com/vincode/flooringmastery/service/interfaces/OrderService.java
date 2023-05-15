package com.vincode.flooringmastery.service.interfaces;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.NoOrdersFoundException;
import com.vincode.flooringmastery.model.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 * The OrderService interface provides methods to perform operations related to orders.
 * It utilizes the ValidateService and EstimateService classes for validation and estimation.
 * After performing the necessary operations, it delegates the call to the DAO to execute the commands.
 */
public interface OrderService {

    /**
     * Gets the list of orders from the dao class.
     *
     * @param date The date for which to retrieve orders.
     * @return A list of orders for the specified date which will be used by the controller
     * @throws NoOrdersFoundException if the order was not found which will be caught by teh controller.
     */
    List<Order> getOrdersByDate(String date);

    /**
     * Delegates the call to the dao class to execute the add order command.
     *
     * @param date  The date for which to add the order.
     * @param order The order to be added.
     * @throws InvalidOrderException If the order is invalid.
     */
    void addOrder(String date, Order order) throws InvalidOrderException;

    /**
     * Delegates the call to dao to retrieve the order.
     *
     * @param date        The date of the order.
     * @param orderNumber The order number.
     * @return The order with the specified date and order number.
     */
    Order getOrder(String date, int orderNumber);

    /**
     * Validates and estimates the order based on the parameters passed in by the user.
     *
     * @param date        The date of the order.
     * @param name        The customer name.
     * @param state       The state of the order.
     * @param area        The area of the order.
     * @param productType The product type of the order.
     *
     * @return The created order if it didn't throw any exceptions.
     * @throws InvalidOrderException If the order is invalid.
     */
    Order createOrder(String date, String name, String state, BigDecimal area, String productType) throws InvalidOrderException;

    /**
     * Delegates the call to dao to update the order in the register and in the file.
     * Then it performs the estimation action on the returned order object.
     *
     * @param date        The date of the order.
     * @param orderNumber The order number.
     * @param name        The customer name.
     * @param state       The state of the order.
     * @param area        The area of the order.
     * @param productType The product type of the order.
     *
     * @return The updated and estimated order that will be displayed to the user.
     * @throws InvalidOrderException If the order is invalid, mean that the exception was thrown by any helping classes.
     */
    Order updateOrder(String date, int orderNumber, String name, String state, BigDecimal area, String productType) throws InvalidOrderException;

    /**
     * Simply validates the fields passed by the user using the validate service.
     *
     * @param name        The customer name.
     * @param state       The state of the order.
     * @param area        The area of the order.
     * @param productType The product type of the order.
     * @throws InvalidOrderException If any of the fields failed the validation.
     */
    void validateOrder(String name, String state, BigDecimal area, String productType) throws InvalidOrderException;

    /**
     * Delegates the call to teh dao to execute the remove order command.
     *
     * @param date        The date of the order.
     * @param orderNumber The order number.
     * @return The removed order.
     * @throws InvalidOrderException If the execution was not successful.
     */
    Order removeOrder(String date, int orderNumber) throws InvalidOrderException;

    /**
     * Exports all orders for the current date.
     * Current date it's simply the exact date when the command was executed.
     *
     * @param now The current date.
     * @throws InvalidOrderException  If the order is invalid.
     */
    void exportAll(LocalDate now) throws IOException, InvalidOrderException;
}
