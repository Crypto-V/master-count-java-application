package com.vincode.flooringmastery.dao;

import com.vincode.flooringmastery.dao.interfaces.TaxRateDao;
import com.vincode.flooringmastery.exceptions.StateNotFoundException;
import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TaxRateDaoImpl implements TaxRateDao {

    Map<String, BigDecimal> stateAndRate = new HashMap<>();

    public TaxRateDaoImpl() {
        //Method will be called automatically which will load the data in the map.
        String PATH = "src/main/resources/data/Taxes.txt";
        readFile(PATH);
    }

    @Override
    public void readFile(String path) {
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            // Skip the first line
            scanner.nextLine();
            // Read the remaining lines
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                stateAndRate.put(values[0].toUpperCase(), new BigDecimal(values[2]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BigDecimal getRate(String stateAbbreviation) {
        BigDecimal tempRate = stateAndRate.get(stateAbbreviation.toUpperCase());
        if (tempRate == null) {
            throw new StateNotFoundException("State not found");
        }
        return tempRate;
    }
}
