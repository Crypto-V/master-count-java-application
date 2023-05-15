package com.vincode.flooringmastery.dao.interfaces;

public interface Reader {

    /**
     * This is the parent interface of product and tax dao,
     * it's requesting from both interfaces to provide implementation for this method based on their custom paths.
     * Finally, it reads the contents of a file and populates the productMap
     * with the product types and their associated costs.
     *
     * @param path The path of the file to be read.
     */
    void readFile(String path);
}
