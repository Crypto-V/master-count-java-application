package com.vincode.flooringmastery.dao.interfaces;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.model.Order;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface OrderDao{

    List<Order> getOrdersByDate(String date);
    void addOrder(String date, Order order) throws InvalidOrderException;
    Order getOrder(String date, int orderNumber);
    Order updateOrder(String date, int orderNumber, String name, String state, BigDecimal area, String productType) throws InvalidOrderException;
    Order removeOrder(String date, int orderNumber) throws InvalidOrderException;
    void export(String date) throws IOException, InvalidOrderException;
}
