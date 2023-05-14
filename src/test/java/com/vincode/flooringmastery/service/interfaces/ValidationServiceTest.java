package com.vincode.flooringmastery.service.interfaces;

import com.vincode.flooringmastery.dao.ProductDaoImpl;
import com.vincode.flooringmastery.dao.TaxRateDaoImpl;
import com.vincode.flooringmastery.dao.interfaces.ProductDao;
import com.vincode.flooringmastery.dao.interfaces.TaxRateDao;
import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.StateNotFoundException;
import com.vincode.flooringmastery.service.ValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationServiceTest {

    private ValidationService validationService;
    private ProductDao productDaoMock;
    private TaxRateDao taxRateDaoMock;

    @BeforeEach
    void setUp() {
        // Create mock objects for the dependencies
        productDaoMock = mock(ProductDaoImpl.class);
        taxRateDaoMock = mock(TaxRateDaoImpl.class);
        // Create ValidationService instance with mock dependencies
        validationService = mock(ValidationServiceImpl.class);


    }


    @Test
    @DisplayName("test date valid")
    void testDateValid() throws InvalidOrderException {

        // ARRANGE
        //Date to be valid should be in the format MMddyyyy or MM-dd-yyyy
        //And it has to be in the future.

        String date = "11-09-2024";

        // ACT
        //if no exception thrown during the validation the test is considered valid.
        assertDoesNotThrow(() -> validationService.validateDate(date));

        // ASSERT
        //Checking if the method was invoked only once which should be the case since the exception will be thrown
        verify(validationService, times(1)).validateDate(date);

    }


    @Test
    @DisplayName("test date invalid entry")
    void testDateInvalidEntry() throws InvalidOrderException {

        // ARRANGE
        String date = "11-09-2021";

        // ACT
        // mocking the behaviour
        doThrow(new InvalidOrderException("--! Order date must be in the future: " + date)).when(validationService).validateDate(date);

        InvalidOrderException exception = assertThrows(InvalidOrderException.class, () -> {
            validationService.validateDate(date);
        });

        // ASSERT
        assertEquals("--! Order date must be in the future: " + date, exception.getMessage());


    }


    @Test
    @DisplayName("test valid name")
    void testValidName() {

        // ARRANGE
        //A valid name based on requirements is ^[a-zA-Z0-9., ]+$
        //^ asserts the start of the string.
        //[a-zA-Z0-9., ] defines a character class that matches any uppercase letter (A-Z),
        // whatlowercase letter (a-z), digit (0-9), period (.), comma (,), or space ( ).
        //+ quantifier specifies that the preceding character class should match one or more times.
        //$ ensuring that full string will comply.
        String name = "Vasile, Verejan";

        // ACT and  ASSERT
        //If no throw than is valid.
        assertDoesNotThrow(() -> validationService.validateName(name));

    }


    @Test
    @DisplayName("test wrong format name")
    void testWrongFormatName() throws InvalidOrderException {

        // ARRANGE
        String name = "_Invalid_Vasile";
        String expectedOutput = "--! Name can't be blank, limited to characters [a-z][0-9] as well as periods and comma characters.";

        // ACT
        //Mocking the behaviour
        doThrow(new InvalidOrderException(expectedOutput)).when(validationService).validateName(name);

        InvalidOrderException exception = assertThrows(InvalidOrderException.class, () -> {
            validationService.validateName(name);
        });

        // ASSERT
        assertEquals(expectedOutput, exception.getMessage());
    }


    @Test
    @DisplayName("test valid state with integration")
    void testValidStateWithIntegration() {
        // ARRANGE
        String state = "TX";
        BigDecimal expectedTaxRate = new BigDecimal("4.45");

        // Set up the mock to return a valid tax rate for the state
        when(taxRateDaoMock.getRate(state)).thenReturn(expectedTaxRate);

        // Create a ValidationService instance with the mock dependencies
        validationService = new ValidationServiceImpl(productDaoMock, taxRateDaoMock);

        // ACT and ASSERT
        // If no exception is thrown, the state is considered valid
        assertDoesNotThrow(() -> validationService.validateState(state));

        // Verify that the TaxRateDao method was called with the expected parameter
        verify(taxRateDaoMock, times(1)).getRate(state);
    }


    @Test
    @DisplayName("test invalid state throws exception")
    void testInValidStateThrowsException() {

        // ARRANGE
        String state = "Some Invalid State";
        BigDecimal expectedTaxRate = new BigDecimal("4.45");

        // Mock the behavior of getRate to throw StateNotFoundException
        when(taxRateDaoMock.getRate(state)).thenThrow(new StateNotFoundException("State not found"));

        // Create a ValidationService instance with the mock dependencies
        validationService = new ValidationServiceImpl(productDaoMock, taxRateDaoMock);

        // ACT
        InvalidOrderException exception = assertThrows(InvalidOrderException.class, () -> {
            validationService.validateState(state);
        });

        // ASSERT
        assertEquals("--! State " + state + " is not supported.", exception.getMessage());
    }


    @Test
    @DisplayName("test area size valid")
    void testAreaSizeValid() throws InvalidOrderException {

        // ARRANGE
        //The area to be valid should be more than 100. Simple unit test will proof.
        BigDecimal areaSize = new BigDecimal("120");

        // ACT and ASSERT
        assertDoesNotThrow(() -> validationService.validateArea(areaSize));
    }


    @Test
    @DisplayName("test area with the value below 100")
    void testAreaWithTheValueBelow100() {

        // ARRANGE
        BigDecimal areaSize = new BigDecimal("99");
        String expectedMessage = "--! Order area must be at least 100 square feet.";

        //Creating the real instance of the service to check the behaviour.
        validationService = new ValidationServiceImpl(productDaoMock, taxRateDaoMock);

        // ACT and ASSERT
        InvalidOrderException exception = assertThrows(InvalidOrderException.class, () -> {
            validationService.validateArea(areaSize);
        });

        // ASSERT
        assertEquals(expectedMessage, exception.getMessage());
    }

    //Integration testing with the real productDaoImpl
    @Test
    @DisplayName("test valid product type")
    void testValidProductType() {

        // ARRANGE
        // Assuming "Carpet" is a valid product type
        String productType = "Wood";
        ProductDao productDao = new ProductDaoImpl();
        validationService = new ValidationServiceImpl(productDao,taxRateDaoMock);

        // ACT and ASSERT
        // If no exception is thrown, the product type is considered valid
        assertDoesNotThrow(() -> {
            validationService.validateProductType(productType);
        });
    }

    @Test
    @DisplayName("test invalid product type throws exception")
    void testInvalidProductTypeThrowsException() {
        // ARRANGE
        String productType = "InvalidProductType"; // Assuming "InvalidProductType" is not a valid product type
        ProductDao productDao = new ProductDaoImpl();
        validationService = new ValidationServiceImpl(productDao,taxRateDaoMock);

        // ACT and ASSERT
        // If an InvalidOrderException is thrown, it indicates that the product type is invalid
        assertThrows(InvalidOrderException.class, () -> {
            validationService.validateProductType(productType);
        });
    }


}