package com.vincode.flooringmastery.service.interfaces;

import com.vincode.flooringmastery.dao.interfaces.ProductDao;
import com.vincode.flooringmastery.dao.interfaces.TaxRateDao;
import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.ProductTypeNotFoundException;
import com.vincode.flooringmastery.exceptions.StateNotFoundException;
import com.vincode.flooringmastery.model.Order;
import com.vincode.flooringmastery.service.EstimationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EstimationServiceTest {

    //Common mock fields
    private final String name = "Doctor Who";
    private final BigDecimal area = new BigDecimal("243.00");
    private final String productType = "Wood";
    private final BigDecimal costPerSqFt = new BigDecimal("5.15");
    private final BigDecimal laborCostPerSqFt = new BigDecimal("4.75");
    private final String state = "WA";
    private final BigDecimal taxRate = new BigDecimal("9.25");
    private EstimationService estimationService;
    private ProductDao productDaoMock;
    private TaxRateDao taxRateDaoMock;

    @BeforeEach
    void setUp() {
        // Create mock objects for the dependencies
        productDaoMock = mock(ProductDao.class);
        taxRateDaoMock = mock(TaxRateDao.class);
        // Create EstimationService instance with mock dependencies
        estimationService = new EstimationServiceImpl(productDaoMock, taxRateDaoMock);

    }

    @Test
    @DisplayName("Test if estimation service instance returns correct value")
    void testIfEstimationServiceInstanceReturnsCorrectValue() throws InvalidOrderException {

        // ARRANGE
        BigDecimal expectedTotal = new BigDecimal("2622.213000");

        // ACT
        when(productDaoMock.getProductCosts(eq(productType))).thenReturn(List.of(costPerSqFt, laborCostPerSqFt));
        when(taxRateDaoMock.getRate(eq(state))).thenReturn(taxRate);

        //Calling the method on estimationService
        Order order = estimationService.calculateEstimates(name, state, area, productType);

        // ASSERT
        assertEquals(name, order.getCustomerName());
        assertEquals(state, order.getState());
        assertEquals(productType, order.getProductType());
        assertEquals(costPerSqFt, order.getCostPerSquareFoot());
        assertEquals(laborCostPerSqFt, order.getLaborCostPerSquareFoot());
        assertEquals(taxRate, order.getTaxRate());
        assertEquals(area, order.getArea());
        assertEquals(expectedTotal, order.getTotal());

    }

    @Test
    @DisplayName("Test calculate estimates throws state not found exception")
    void testCalculateEstimatesThrowsStateNotFoundException() {

        // ARRANGE mock behavior
        String state = "Invalid State";


        // ACT
        when(productDaoMock.getProductCosts(eq(productType))).thenReturn(List.of(costPerSqFt, laborCostPerSqFt));
        //show throw state not found exception
        when(taxRateDaoMock.getRate(eq(state))).thenThrow(new StateNotFoundException("State not found"));

        // ASSERT
        //Should throw a wrapping exception InvalidOrderException
        assertThrows(InvalidOrderException.class, () -> estimationService.calculateEstimates(name, state, area, productType));
    }

    @Test
    @DisplayName("Test calculate estimates throws product type not found exception")
    void testCalculateEstimatesThrowsProductTypeNotFoundException() {

        // ARRANGE mock behavior
        String productType = "Pillows";

        // ACT
        when(taxRateDaoMock.getRate(eq(state))).thenReturn(taxRate);

        //Will throw a ProductTypeNotFoundException and when caught will throw InvalidOrderException
        when(productDaoMock.getProductCosts(eq(productType))).thenThrow(new ProductTypeNotFoundException("Product type: " + productType + " was not found!"));

        // ASSERT
        assertThrows(InvalidOrderException.class, () -> estimationService.calculateEstimates(name, state, area, productType));
    }

}