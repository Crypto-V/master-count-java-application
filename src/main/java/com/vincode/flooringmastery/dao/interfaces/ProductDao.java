package com.vincode.flooringmastery.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends Reader {

    List<BigDecimal> getProductCosts(String productType);


}
