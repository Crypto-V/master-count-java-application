package com.vincode.flooringmastery.dao;

import com.vincode.flooringmastery.dao.interfaces.ProductDao;
import com.vincode.flooringmastery.exceptions.ProductTypeNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoImpl implements ProductDao {

    Map<String, List<BigDecimal>> productMap = new HashMap<>();

    public ProductDaoImpl() {
        //Method will be called automatically which will load the data in the map.
        String PATH = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\main\\resources\\data\\Products.txt";
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
                List<BigDecimal> costs = new ArrayList<>();
                costs.add(new BigDecimal(values[1]));
                costs.add(new BigDecimal(values[2]));
                productMap.put(values[0].toUpperCase(), costs);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BigDecimal> getProductCosts(String productType) {
        List<BigDecimal> tempList = productMap.get(productType.toUpperCase());
        if (tempList == null || tempList.isEmpty()) {
            throw new ProductTypeNotFoundException("Product type: " + productType+" was not found!");
        }

        return tempList;
    }


}
