package com.vincode.flooringmastery.dao.interfaces;

import com.vincode.flooringmastery.dao.TaxRateDaoImpl;
import com.vincode.flooringmastery.exceptions.ProductTypeNotFoundException;
import com.vincode.flooringmastery.exceptions.StateNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxRateDaoTest {
    TaxRateDao taxRateDao;

    @BeforeEach
    void setUp() {
        taxRateDao = new TaxRateDaoImpl();
    }


    @Test
    @DisplayName("Test geting the tax rate for a state")
    void testGetingTheTaxRateForAState() {

        // ARRANGE
        String state = "WA";
        BigDecimal desiredValue = new BigDecimal("9.25");

        // ACT
        BigDecimal actual = taxRateDao.getRate(state);


        // ASSERT
        assertEquals(desiredValue, actual);
    }


    @Test
    @DisplayName("Test with wrong value throws exception")
    void testWithWrongValueThrowsException() {

        // ARRANGE
        String state = "VA";

        // ACT and ASSERT
        Assertions.assertThrows(StateNotFoundException.class, () -> {
            taxRateDao.getRate(state);
        });
    }


}