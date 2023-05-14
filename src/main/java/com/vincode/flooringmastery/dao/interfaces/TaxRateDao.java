package com.vincode.flooringmastery.dao.interfaces;

import java.math.BigDecimal;

public interface TaxRateDao extends Reader {

    BigDecimal getRate(String stateAbbreviation) ;
}
