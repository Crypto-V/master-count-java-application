package com.vincode.flooringmastery.dao.interfaces;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.NoOrdersFoundException;
import com.vincode.flooringmastery.model.Order;
import com.vincode.flooringmastery.model.OrderStamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockOrderDaoImpl implements OrderDao {

    private final Map<String, OrderStamp> mockRegister;
    String ordersPath = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\main\\resources\\orders";
    private int latestOrderNumber = 0;

    public MockOrderDaoImpl(String orderDirectory) {
        mockRegister = new HashMap<>();
    }

    @Override
    public List<Order> getOrdersByDate(String date) {
        OrderStamp orderStamp = mockRegister.get(date);
        if (orderStamp != null && orderStamp.getOrderDetail() != null) {
            return Collections.singletonList(orderStamp.getOrderDetail());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void addOrder(String date, Order order) throws InvalidOrderException {

        //Creating the file with the name following the conventions and assigning the desired path.
        String fileName = "Orders_" + date + ".txt";
        String ORDER_DIR = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\main\\resources\\orders\\";
        Path filePath = Paths.get(ORDER_DIR + "/" + fileName);

        //Setting the order number to be the next number;
        int nextOrderNumber = ++latestOrderNumber;
        order.setOrderNumber(nextOrderNumber);

        //Keeping track of the added file in the register for fast retrieval.
        registerOrder(date, order);

    }

    @Override
    public Order getOrder(String date, int orderNumber) {
        OrderStamp os = mockRegister.get(date);
        if (os != null) {
            Order order = os.getOrderDetail();
            if (order.getOrderNumber() == orderNumber) {
                return order;
            } else {
                throw new NoOrdersFoundException("Order with this number: " + orderNumber + " was not found!");
            }
        } else {
            throw new NoOrdersFoundException("Order with this date: " + date + " was not found!");
        }
    }

    @Override
    public Order updateOrder(String date, int orderNumber, String name, String state, BigDecimal area, String productType) throws NoOrdersFoundException {
        OrderStamp os = mockRegister.get(date);
        if (os != null) {
            Order existingOrder = os.getOrderDetail();
            if (existingOrder.getOrderNumber() == orderNumber) {
                existingOrder.setCustomerName(name);
                existingOrder.setState(state);
                existingOrder.setArea(area);
                existingOrder.setProductType(productType);
                return existingOrder; // Return the updated order
            }
        }
        throw new NoOrdersFoundException("Order with this date and order number combination was not found!");
    }


    @Override
    public Order removeOrder(String date, int orderNumber) throws NoOrdersFoundException {

        if (!mockRegister.containsKey(date)) {
            throw new NoOrdersFoundException("No order found for the given date: " + date);
        }

        OrderStamp os = mockRegister.get(date);
        if (os.getOrderNumber() != orderNumber) {
            throw new NoOrdersFoundException("Invalid order number: " + orderNumber);
        }

        mockRegister.remove(date);

        return os.getOrderDetail();
    }

    @Override
    public void export(String date) {
        String fileName = "DataExport" + date + ".txt";
        String ORDER_DIR = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\main\\resources\\backup\\";
        Path filePath = Paths.get(ORDER_DIR + "/" + fileName);

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            for (OrderStamp os : mockRegister.values()) {
                writer.println(os.getOrderDetail().toExportString());
            }
        } catch (IOException e) {
            System.out.println("\n!-- An error occurred while performing file operations. Check the path!");
        }
    }

    //Helping methods:
    //Creating a register for all files that will be added for easy access.
    private void registerOrder(String date, Order order) throws InvalidOrderException {
        if (!mockRegister.containsKey(date)) {
            OrderStamp stamp = new OrderStamp(date, order.getOrderNumber(), order);
            mockRegister.put(stamp.getDate(), stamp);
        } else {
            throw new InvalidOrderException("We are fully booked for this dates:  \n" + mockRegister.keySet() + "\n Try selecting another date!");
        }
    }


}
