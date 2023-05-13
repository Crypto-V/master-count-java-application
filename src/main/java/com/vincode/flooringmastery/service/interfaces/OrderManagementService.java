package com.vincode.flooringmastery.service.interfaces;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.model.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderManagementService {

    List<Order> getOrdersByDate(String date);

    void addOrder(String date, Order order) throws InvalidOrderException;

    Order getOrder(String date, int orderNumber);

    Order createOrder(String date, String name, String state, BigDecimal area, String productType) throws InvalidOrderException;

    Order updateOrder(String date, int orderNumber, String name, String state, BigDecimal area, String productType) throws InvalidOrderException;

    void validateOrder(String name, String state, BigDecimal area, String productType) throws InvalidOrderException;

    Order removeOrder(String date, int orderNumber) throws InvalidOrderException;

    void exportAll(LocalDate now) throws IOException, InvalidOrderException;
}
