package com.vincode.flooringmastery.dao.interfaces;

import com.vincode.flooringmastery.dao.ProductDaoImpl;
import com.vincode.flooringmastery.exceptions.ProductTypeNotFoundException;
import com.vincode.flooringmastery.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoTest {

    ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDaoImpl();
        //The necessary file will be read and will be ready for use.
    }


    @Test
    @DisplayName("Test to get the product cost carpet")
    void testToGetTheProductCostCarpet() {

        // ARRANGE
        String key = "Carpet";
        BigDecimal  costPerSquareFoot = new BigDecimal("2.25");
        BigDecimal  laborCostPerSquareFoot = new BigDecimal("2.10");

        // ACT
        List<BigDecimal> productsCost = productDao.getProductCosts(key);
        BigDecimal actualCostPerSquareFoot = productsCost.get(0);
        BigDecimal actualLaborCostPerSquareFoot = productsCost.get(1);

        // ASSERT
        assertEquals(costPerSquareFoot,actualCostPerSquareFoot);
        assertEquals(laborCostPerSquareFoot,actualLaborCostPerSquareFoot);
    }

    @Test
    @DisplayName("Test to get the product cost laminate")
    void testToGetTheProductCostLaminate() {

        // ARRANGE
        String key = "Laminate";
        BigDecimal  costPerSquareFoot = new BigDecimal("1.75");
        BigDecimal  laborCostPerSquareFoot = new BigDecimal("2.10");

        // ACT
        List<BigDecimal> productsCost = productDao.getProductCosts(key);
        BigDecimal actualCostPerSquareFoot = productsCost.get(0);
        BigDecimal actualLaborCostPerSquareFoot = productsCost.get(1);

        // ASSERT
        assertEquals(costPerSquareFoot,actualCostPerSquareFoot);
        assertEquals(laborCostPerSquareFoot,actualLaborCostPerSquareFoot);
    }

    @Test
    @DisplayName("Test to get the product cost tile")
    void testToGetTheProductCostTile() {

        // ARRANGE
        String key = "Tile";
        BigDecimal  costPerSquareFoot = new BigDecimal("3.50");
        BigDecimal  laborCostPerSquareFoot = new BigDecimal("4.15");

        // ACT
        List<BigDecimal> productsCost = productDao.getProductCosts(key);
        BigDecimal actualCostPerSquareFoot = productsCost.get(0);
        BigDecimal actualLaborCostPerSquareFoot = productsCost.get(1);

        // ASSERT
        assertEquals(costPerSquareFoot,actualCostPerSquareFoot);
        assertEquals(laborCostPerSquareFoot,actualLaborCostPerSquareFoot);
    }

    @Test
    @DisplayName("Test to get the product cost wood")
    void testToGetTheProductCostWood() {

        // ARRANGE
        String key = "Wood";
        BigDecimal  costPerSquareFoot = new BigDecimal("5.15");
        BigDecimal  laborCostPerSquareFoot = new BigDecimal("4.75");

        // ACT
        List<BigDecimal> productsCost = productDao.getProductCosts(key);
        BigDecimal actualCostPerSquareFoot = productsCost.get(0);
        BigDecimal actualLaborCostPerSquareFoot = productsCost.get(1);

        // ASSERT
        assertEquals(costPerSquareFoot,actualCostPerSquareFoot);
        assertEquals(laborCostPerSquareFoot,actualLaborCostPerSquareFoot);
    }


    @Test
    @DisplayName("Testing the wrong product type for exception ")
    void testingTheWrongProductTypeForException() {

        // ARRANGE
        String key = "Pillows";

        // ACT and ASSERT
        Assertions.assertThrows(ProductTypeNotFoundException.class, () -> {
            productDao.getProductCosts(key);
        });
    }


}