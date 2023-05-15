package com.vincode.flooringmastery.dao.interfaces;

import java.math.BigDecimal;

public interface TaxRateDao extends Reader {

    /**
     * Retrieves the tax associated with the specified product type.
     *
     * @param stateAbbreviation The abbreviation of the state the user wants to place the order for.
     * @return A  BigDecimal value representing the tax associated with the specified state.
     */
    BigDecimal getRate(String stateAbbreviation) ;
}
