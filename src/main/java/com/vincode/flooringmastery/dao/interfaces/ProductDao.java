package com.vincode.flooringmastery.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends Reader {

    /**
     * Retrieves the costs associated with the specified product type.
     *
     * @param productType The type of product for which to retrieve the costs.
     * @return A list of BigDecimal values representing the costs associated with the specified product type.
     */
    List<BigDecimal> getProductCosts(String productType);


}
