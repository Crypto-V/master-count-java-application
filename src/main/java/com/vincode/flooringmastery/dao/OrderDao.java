package com.vincode.flooringmastery.dao;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao{

    List<Order> getOrdersByDate(String date);
    Order addOrder(String date,Order order) throws InvalidOrderException;
    Order getOrder(String date, int orderNumber);
    Order removeTheOrder(String date, Long orderNumber);
    void export();
}
