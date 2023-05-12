package com.vincode.flooringmastery.exceptions;

public class StateNotFoundException extends RuntimeException {

    public StateNotFoundException(String stateAbbreviation) {

        super("Product type '" + stateAbbreviation + "' was not found!");
    }
}
