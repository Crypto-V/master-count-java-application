package com.vincode.flooringmastery.service.interfaces;

import com.vincode.flooringmastery.dao.interfaces.MockOrderDaoImpl;
import com.vincode.flooringmastery.dao.interfaces.OrderDao;
import com.vincode.flooringmastery.service.EstimationServiceImpl;
import com.vincode.flooringmastery.service.ValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

class OrderServiceTest {

    OrderDao orderDao;
    ValidationService validationService;
    EstimationService estimationService;

    @BeforeEach
    void setUp() {
        String orderDirectory = "path/to/mock/directory";
        orderDao = new MockOrderDaoImpl(orderDirectory);
        validationService = mock(ValidationServiceImpl.class);
        estimationService = mock(EstimationServiceImpl.class);
    }


//    Order getOrder(String date, int orderNumber);
//
//    Order createOrder(String date, String name, String state, BigDecimal area, String productType) throws InvalidOrderException;
//
//    void validateOrder(String name, String state, BigDecimal area, String productType) throws InvalidOrderException;
//
//    void exportAll(LocalDate now) throws IOException, InvalidOrderException;




}