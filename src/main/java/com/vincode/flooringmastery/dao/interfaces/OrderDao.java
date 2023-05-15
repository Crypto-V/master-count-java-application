package com.vincode.flooringmastery.dao.interfaces;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.model.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface OrderDao{

    /**
     * Retrieves a list of orders for the specified date.
     * This method retrieves all the orders associated with the provided date and returns them as a list.
     *
     * @param date the date for which to retrieve the orders
     * @return a list of orders for the specified date
     *
     */
    List<Order> getOrdersByDate(String date);

    /**
     * Adds an order for the specified date.
     * This method adds the provided order to the collection of orders associated with the specified date.
     *
     * @param date the date for which to add the order
     * @param order the order to be added
     * @throws InvalidOrderException if the order is invalid or cannot be added
     */
    void addOrder(String date, Order order) throws InvalidOrderException;

    /**
     * Retrieves an order for the specified date and order number.
     * This method retrieves the order with the given order number associated with the provided date.
     *
     * @param date the date of the order
     * @param orderNumber the order number to retrieve
     * @return the order with the specified order number for the given date, or null if not found
     */
    Order getOrder(String date, int orderNumber);

    /**
     * Updates an existing order with new information.
     * This method updates the order with the given order number associated with the provided date.
     *
     * @param date the date of the order
     * @param orderNumber the order number to update
     * @param name the new customer name
     * @param state the new state
     * @param area the new area
     * @param productType the new product type
     * @return the updated order
     * @throws InvalidOrderException if the order is invalid or cannot be updated
     */
    Order updateOrder(String date, int orderNumber, String name, String state, BigDecimal area, String productType) throws InvalidOrderException;

    /**
     * Removes an order for the specified date and order number.
     * This method removes the order with the given order number associated with the provided date
     * and returns the removed order.
     *
     * @param date the date of the order
     * @param orderNumber the order number to remove
     * @return the removed order
     * @throws InvalidOrderException if the order cannot be removed
     */
    Order removeOrder(String date, int orderNumber) throws InvalidOrderException;

    /**
     * Exports orders for the specified date.
     * This method exports the orders associated with the provided date to an external file or destination.
     * The exported orders can be used for backup, sharing, or other purposes.
     *
     * @param date current date.
     * @throws IOException if an I/O error occurs during export
     * @throws InvalidOrderException if the order data is invalid or cannot be exported.
     */
    void export(String date) throws IOException, InvalidOrderException;
}
