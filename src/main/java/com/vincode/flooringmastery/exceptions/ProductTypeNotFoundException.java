package com.vincode.flooringmastery.exceptions;

public class ProductTypeNotFoundException extends RuntimeException{

    public ProductTypeNotFoundException(String productType) {

        super("Product type '" + productType + "' was not found!");
    }
}
