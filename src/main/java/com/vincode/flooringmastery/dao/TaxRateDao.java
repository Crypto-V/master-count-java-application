package com.vincode.flooringmastery.dao;

import java.math.BigDecimal;

public interface TaxRateDao extends Reader {

    BigDecimal getRate(String stateAbbreviation);
}
