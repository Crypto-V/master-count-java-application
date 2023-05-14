package com.vincode.flooringmastery.dao.interfaces;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderDaoImplTest {

    private MockOrderDaoImpl orderDao;

    @BeforeEach
    public void setup() {
        // Set up the mock order DAO with the desired directory for testing
        String orderDirectory = "path/to/mock/directory";
        orderDao = new MockOrderDaoImpl(orderDirectory);
    }

    @Test
    @DisplayName("test add order")
    void testAddOrder() throws InvalidOrderException {

        // ARRANGE
        // Create an order and all the date from one of the real orders
        Order order = new Order();
        order.setCustomerName("STEVEN JHNOSON");
        order.setState("TX");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("WOOD");
        order.setArea(new BigDecimal("450"));
        order.setCostPerSquareFoot(new BigDecimal("5.15"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order.setMaterialCost(new BigDecimal("2317.50"));
        order.setLaborCost(new BigDecimal("2137.50"));
        order.setTax(new BigDecimal("178.2000"));
        order.setTotal(new BigDecimal("4633.2000"));

        // ACT
        orderDao.addOrder("13052024", order);

        // ASSERT
        // Verify that the order was added
        List<Order> orders = orderDao.getOrdersByDate("13052024");
        //If the orders size will be one it means that order was added.
        Assertions.assertEquals(1, orders.size());
        Order addedOrder = orders.get(0);

        Assertions.assertEquals(order.getCustomerName(), addedOrder.getCustomerName());
        Assertions.assertEquals(order.getState(), addedOrder.getState());
        Assertions.assertEquals(order.getArea(), addedOrder.getArea());
        Assertions.assertEquals(order.getTaxRate(), addedOrder.getTaxRate());
        Assertions.assertEquals(order.getProductType(), addedOrder.getProductType());
        Assertions.assertEquals(order.getCostPerSquareFoot(), addedOrder.getCostPerSquareFoot());
        Assertions.assertEquals(order.getLaborCostPerSquareFoot(), addedOrder.getLaborCostPerSquareFoot());
        Assertions.assertEquals(order.getMaterialCost(), addedOrder.getMaterialCost());
        Assertions.assertEquals(order.getLaborCost(), addedOrder.getLaborCost());
        Assertions.assertEquals(order.getTax(), addedOrder.getTax());
        Assertions.assertEquals(order.getTotal(), addedOrder.getTotal());

    }


    @Test
    @DisplayName("test get order by date and order number")
    void testGetOrderByDateAndOrderNumber() throws InvalidOrderException {

        // ARRANGE
        //When new object is created will default to order number 1;
        Order order1 = new Order();

        // ACT
        orderDao.addOrder("13052024", order1);
        Order order = orderDao.getOrder("13052024", 1);

        // ASSERT
        Assertions.assertNotNull(order);
        Assertions.assertEquals(1, order.getOrderNumber());
    }

    @Test
    @DisplayName("test update order with prefilled values")
    void testUpdateOrderWithPrefilledValues() throws InvalidOrderException {

        // ARRANGE
        // Creating the object and setting some fields.
        Order tempOrder = new Order();
        tempOrder.setCustomerName("Peter");
        tempOrder.setState("TX");
        tempOrder.setArea(new BigDecimal("200"));
        tempOrder.setProductType("Wood");

        // Defining the updated fields.
        String updatedName = "Vasile Verejan";
        String updatedState = "CA";
        BigDecimal updatedArea = new BigDecimal("600");
        String updatedProductType = "Carpet";

        // Adding the order
        orderDao.addOrder("13052024", tempOrder);

        // ACT
        // The first object created will always be order number 1.
        orderDao.updateOrder("13052024", 1, updatedName, updatedState, updatedArea, updatedProductType);

        // Get the order back
        Order retrievedOrder = orderDao.getOrder("13052024", 1);

        // ASSERT
        Assertions.assertEquals(updatedName, retrievedOrder.getCustomerName());
        Assertions.assertEquals(updatedState, retrievedOrder.getState());
        Assertions.assertEquals(updatedArea, retrievedOrder.getArea());
        Assertions.assertEquals(updatedProductType, retrievedOrder.getProductType());

        // Based on the requirements date and order Number don't have to change, lets check.
        Assertions.assertEquals(1, retrievedOrder.getOrderNumber());
    }


    @Test
    @DisplayName("test remove order")
    void testRemoveOrder() throws InvalidOrderException {
        // ARRANGE
        String date = "13052024";
        int orderNumber = 1;

        // Create an order and add it to the orderDao
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        orderDao.addOrder(date, order);

        // ACT
        Order removedOrder = orderDao.removeOrder(date, orderNumber);

        // ASSERT
        // Verify that the order was removed
        Assertions.assertEquals(orderNumber, removedOrder.getOrderNumber());
        // ... assert other removed order properties

        // Verify that the order no longer exists
        List<Order> orders = orderDao.getOrdersByDate(date);
        assertTrue(orders.isEmpty());
    }


    @Test
    @DisplayName("test export feature to see if the file was created in the backup folder")
    void testExportFeature() throws IOException, InvalidOrderException {

        //Will use the real dao implementation to check if the file will be exported, so will check if there
        // is a file with the given name.

        // ARRANGE
        String date = "12032025";

        //Adding an order to have what to write in the file except the headings.
        Order order = new Order();
        order.setCustomerName("Vasile Verejan");
        order.setState("TX");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("WOOD");
        order.setArea(new BigDecimal("450"));
        order.setCostPerSquareFoot(new BigDecimal("5.15"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order.setMaterialCost(new BigDecimal("2317.50"));
        order.setLaborCost(new BigDecimal("2137.50"));
        order.setTax(new BigDecimal("178.2000"));
        order.setTotal(new BigDecimal("4633.2000"));

        orderDao.addOrder("13052024", order);

        // Set up expected file name and content
        String expectedFileName = "DataExport" + date + ".txt";

        // ACT

        orderDao.export(date);

        // ASSERT
        String expectedFilePath = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\test\\resources\\backup/" + expectedFileName;
        File exportedFile = new File(expectedFilePath);
        assertTrue(exportedFile.exists());

    }


}

