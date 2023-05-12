package com.vincode.flooringmastery.service;

import com.vincode.flooringmastery.dao.OrderDao;
import com.vincode.flooringmastery.dao.ProductDao;
import com.vincode.flooringmastery.dao.TaxRateDao;
import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.NoOrdersFoundException;

import com.vincode.flooringmastery.model.Order;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {
    private final OrderDao orderDao;
    private final OrderValidationService validationService;
    private final OrderEstimationService estimationService;

    public OrderService(OrderDao orderDao, OrderValidationService orderValidationService, OrderEstimationService orderEstimationService) {
        this.orderDao = orderDao;
        this.validationService = orderValidationService;
        this.estimationService = orderEstimationService;
    }

    public List<Order> getOrdersByDate(String date) {
        List<Order> tempOrders = orderDao.getOrdersByDate(date);
        if (tempOrders.isEmpty()) {
            throw new NoOrdersFoundException("No order found for this date! " + date);
        }
        return tempOrders;
    }

    public void addOrder(String date, Order order) throws InvalidOrderException {
        orderDao.addOrder(date, order);
    }

    public Order getOrder(String date, int orderNumber) throws InvalidOrderException {
        validationService.validateDate(date);
        Order tempOrder = orderDao.getOrder(date, orderNumber);
        if (tempOrder == null) {
            throw new NoOrdersFoundException("Order was not found");
        }
        return tempOrder;
    }

    public Order createOrder(String date, String name, String state, BigDecimal area, String productType) throws InvalidOrderException {
        // Validate order details
        validationService.validateDate(date);
        validationService.validateName(name);
        validationService.validateState(state);
        validationService.validateArea(area);
        validationService.validateProductType(productType);
        // Estimate order
        return estimationService.calculateEstimates(name, state, area, productType);
    }
}

//    public void editOrder(Order order) throws InvalidOrderException {
//        Order validatedOrder = orderValidationService.validateOrder(order);
//        orderEstimationService.calculateEstimates(validatedOrder);
//        orderDao.updateOrder(validatedOrder);
//    }
//
//    public void removeOrder(Order order) {
//        orderDao.removeOrder(order);
//
