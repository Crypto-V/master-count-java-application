package com.vincode.flooringmastery.service.interfaces;

import com.vincode.flooringmastery.dao.interfaces.MockOrderDaoImpl;
import com.vincode.flooringmastery.dao.interfaces.OrderDao;
import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.model.Order;
import com.vincode.flooringmastery.service.EstimationServiceImpl;
import com.vincode.flooringmastery.service.OrderServiceImpl;
import com.vincode.flooringmastery.service.ValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    OrderDao orderDao;
    ValidationService validationService;
    EstimationService estimationService;
    OrderService orderService;

    @BeforeEach
    void setUp() {
        String orderDirectory = "path/to/mock/directory";
        orderDao = new MockOrderDaoImpl(orderDirectory);
        validationService = mock(ValidationServiceImpl.class);
        estimationService = mock(EstimationServiceImpl.class);
        orderService = new OrderServiceImpl(orderDao, validationService, estimationService);
    }


    @Test
    @DisplayName("test get order method to return the order.")
    void testGetOrderMethodToReturnTheOrder() throws InvalidOrderException {

        // ARRANGE

        String date = "11122024";
        Order mockOrder = new Order();
        mockOrder.setCustomerName("STEVEN JHNOSON");
        mockOrder.setState("TX");
        mockOrder.setTaxRate(new BigDecimal("4.45"));
        mockOrder.setProductType("WOOD");
        mockOrder.setArea(new BigDecimal("450"));
        mockOrder.setCostPerSquareFoot(new BigDecimal("5.15"));
        mockOrder.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        mockOrder.setMaterialCost(new BigDecimal("2317.50"));
        mockOrder.setLaborCost(new BigDecimal("2137.50"));
        mockOrder.setTax(new BigDecimal("178.2000"));
        mockOrder.setTotal(new BigDecimal("4633.2000"));

        //When new object is created it starts with number 1.
        int expectedOrderNumber = 1;

        // ACT
        orderService.addOrder(date, mockOrder);
        Order newOrder = orderService.getOrder(date, expectedOrderNumber);

        // ASSERT some values to check if is ok.
        assertEquals(expectedOrderNumber, newOrder.getOrderNumber());
        assertEquals("TX", newOrder.getState());
        assertEquals(new BigDecimal("4633.2000"), newOrder.getTotal());
    }


    @Test
    @DisplayName("testing the createOrder method returns a validated order")
    void testingTheCreateOrderMethodReturnsAValidatedOrder() throws InvalidOrderException {
        // ARRANGE
        String date = "11122024";
        String name = "STEVEN JHNOSON";
        String state = "TX";
        BigDecimal area = new BigDecimal("450");
        String productType = "WOOD";

        Order mockOrder = new Order();
        mockOrder.setCustomerName("STEVEN JHNOSON");
        mockOrder.setState("TX");
        mockOrder.setTaxRate(new BigDecimal("4.45"));
        mockOrder.setProductType("WOOD");
        mockOrder.setArea(new BigDecimal("450"));
        mockOrder.setCostPerSquareFoot(new BigDecimal("5.15"));
        mockOrder.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        mockOrder.setMaterialCost(new BigDecimal("2317.50"));
        mockOrder.setLaborCost(new BigDecimal("2137.50"));
        mockOrder.setTax(new BigDecimal("178.2000"));
        mockOrder.setTotal(new BigDecimal("4633.2000"));

        // ACT
        when(estimationService.calculateEstimates(name, state, area, productType)).thenReturn(mockOrder);

        Order calculatedOrder = assertDoesNotThrow(() -> orderService.createOrder(date, name, state, area, productType));

        // ASSERT
        //Few assertions to double-check that order was created.
        assertEquals(name, calculatedOrder.getCustomerName());
        assertEquals(state, calculatedOrder.getState());
        assertEquals(area, calculatedOrder.getArea());
        assertEquals(productType, calculatedOrder.getProductType());
        assertEquals(new BigDecimal("4633.2000"), calculatedOrder.getTotal());

    }


    @Test
    @DisplayName("test validate order method returns InvalidOrderException")
    void testValidateOrderMethodReturnsInvalidOrderException() throws InvalidOrderException {

        //This test is meant only to simply isolate the validate order method to see if the correct error will be thrown.

        // ARRANGE
        String name = "STEVEN JHNOSON";
        String state = "Shouldn't pass";
        BigDecimal area = new BigDecimal("99");
        String productType = "WOOD";

        //mocking the validation service
        doThrow(new InvalidOrderException("--! Name can't be blank, limited to characters [a-z][0-9] as well as periods and comma characters."))
                .when(validationService).validateName(name);

        doThrow(new InvalidOrderException("--! State " + state + " is not supported."))
                .when(validationService).validateState(state);

        doThrow(new InvalidOrderException("--! Order area must be at least " + area + " square feet."))
                .when(validationService).validateArea(area);

        doThrow(new InvalidOrderException("--! Product type:  " + productType + " is not supported."))
                .when(validationService).validateProductType(productType);


        // ACT and ASSERT
        assertThrows(InvalidOrderException.class, () -> {
            orderService.validateOrder(name,state,area,productType);
        });

    }


    @Test
    @DisplayName("test export from the service layer")
    void testExportFromTheServiceLayer() throws InvalidOrderException, IOException {
        // ARRANGE
        LocalDate date = LocalDate.parse("2029-05-10");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String formattedDate = date.format(formatter).replaceAll("-","");

        OrderDao orderDao = mock(OrderDao.class);
        OrderService orderService = new OrderServiceImpl(orderDao, validationService, estimationService);

        // ACT
        orderService.exportAll(date);

        // ASSERT
        verify(orderDao).export(formattedDate);
    }






}