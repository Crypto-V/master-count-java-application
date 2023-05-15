package com.vincode.flooringmastery.dao;

import com.vincode.flooringmastery.dao.interfaces.OrderDao;
import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.NoOrdersFoundException;
import com.vincode.flooringmastery.model.Order;
import com.vincode.flooringmastery.model.OrderStamp;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class OrderDaoImpl implements OrderDao {
    private final Map<String, OrderStamp> register;
    String ordersPath = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\main\\resources\\orders";
    private int latestOrderNumber = 0;


    public OrderDaoImpl() {
        register = new HashMap<>();
        readFolderFiles();
    }

    @Override
    public List<Order> getOrdersByDate(String date) {
        OrderStamp orderStamp = register.get(date);
        if (orderStamp != null && orderStamp.getOrderDetail() != null) {
            return Collections.singletonList(orderStamp.getOrderDetail());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void addOrder(String date, Order order) throws InvalidOrderException {

        //Creating the file with the name following the conventions and assigning the desired path.
        String fileName = "Orders_" + date + ".txt";
        String ORDER_DIR = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\main\\resources\\orders\\";
        Path filePath = Paths.get(ORDER_DIR + "/" + fileName);

        //Setting the order number to be the next number;
        int nextOrderNumber = ++latestOrderNumber;
        order.setOrderNumber(nextOrderNumber);

        //Write the order to a file, if not found throw InvalidOrderException
        writeOrderToFile(order, filePath);

        //Keeping track of the added file in the register for fast retrieval.
        registerOrder(date, order);

    }

    @Override
    public Order getOrder(String date, int orderNumber) {
        OrderStamp os = register.get(date);
        if (os != null) {
            Order order = os.getOrderDetail();
            if (order.getOrderNumber() == orderNumber) {
                return order;
            } else {
                throw new NoOrdersFoundException("Order with this number: " + orderNumber + " was not found!");
            }
        } else {
            throw new NoOrdersFoundException("Order with this date: " + date + " was not found!");
        }
    }

    @Override
    public Order updateOrder(String date, int orderNumber, String name, String state, BigDecimal area, String productType) throws NoOrdersFoundException, InvalidOrderException {
        OrderStamp os = register.get(date);
        if (os != null) {
            Order existingOrder = os.getOrderDetail();
            if (existingOrder.getOrderNumber() == orderNumber) {
                existingOrder.setCustomerName(name);
                existingOrder.setState(state);
                existingOrder.setArea(area);
                existingOrder.setProductType(productType);

                String fileName = "Orders_" + date + ".txt";
                String ORDER_DIR = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\main\\resources\\orders\\";
                Path filePath = Paths.get(ORDER_DIR + "/" + fileName);

                try {
                    writeOrderToFile(existingOrder, filePath);
                    return existingOrder;
                } catch (InvalidOrderException e) {
                    e.printStackTrace();
                    throw new InvalidOrderException("Failed to update the order.");
                }
            }
        }
        throw new NoOrdersFoundException("Order with this date and order number combination was not found!");
    }

    @Override
    public Order removeOrder(String date, int orderNumber) throws NoOrdersFoundException {

        if (!register.containsKey(date)) {
            throw new NoOrdersFoundException("No order found for the given date: " + date);
        }

        OrderStamp os = register.get(date);
        if (os.getOrderNumber() != orderNumber) {
            throw new NoOrdersFoundException("Invalid order number: " + orderNumber);
        }

        register.remove(date);
        removeOrderFromFile(date);

        return os.getOrderDetail();
    }

    @Override
    public void export(String date) {
        String fileName = "DataExport" + date + ".txt";
        String ORDER_DIR = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\main\\resources\\backup\\";
        Path filePath = Paths.get(ORDER_DIR + "/" + fileName);

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            for (OrderStamp os : register.values()) {
                writer.println(os.getOrderDetail().toExportString());
            }
        }catch (IOException e){
            System.out.println("\n!-- An error occurred while performing file operations. Check the path!");
        }
    }

    //Helping methods.

    /**
     * Registers an order for easy access.
     * This method creates a register for all files by associating a date with an order.
     * A new entry is created with the date, order number, and order object each time user adds an order.
     * If the date already exists in the register, an InvalidOrderException
     * is thrown with a message indicating that the date is already fully booked.
     *
     * @param date the date associated with the order
     * @param order the order to be registered
     * @throws InvalidOrderException if the date is already registered in the order register
     */
    private void registerOrder(String date, Order order) throws InvalidOrderException {
        if (!register.containsKey(date)) {
            OrderStamp stamp = new OrderStamp(date, order.getOrderNumber(), order);
            register.put(stamp.getDate(), stamp);
        } else {
            throw new InvalidOrderException("We are fully booked for this dates:  \n" + register.keySet()+"\n Try selecting another date!");
        }
    }


    /**
     * Writes an order to a file.
     * This method writes the provided order details to the specified file path.
     * All data are written as a single line in the file.
     *
     * @param order the order to be written to the file
     * @param filePath the path of the file to write the order to
     * @throws InvalidOrderException if the file is not found or there is an IOException while writing to the file.
     */
    private void writeOrderToFile(Order order, Path filePath) throws InvalidOrderException {

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            writer.println(
                    order.getOrderNumber() + "," +
                            order.getCustomerName() + "," +
                            order.getState() + "," +
                            order.getTaxRate() + "," +
                            order.getProductType() + "," +
                            order.getArea() + "," +
                            order.getCostPerSquareFoot() + "," +
                            order.getLaborCostPerSquareFoot() + "," +
                            order.getMaterialCost() + "," +
                            order.getLaborCost() + "," +
                            order.getTax() + "," +
                            order.getTotal());
        } catch (IOException e) {
            throw new InvalidOrderException("File not found! Can't write order to the file!" + filePath);
        }
    }


    /**
     * Reads files from a folder and populates the register with order references.
     * This method reads all files from the specified folder path and populates the register
     * with references to the orders found in those files.
     * The method also keeps track of the latest order number encountered.
     * This method is run when the new instance of this class is created to make sure when the user
     * will perform operations on the register it will be preloaded with all existing orders.
     *
     */
    private void readFolderFiles() {
        Path folderPath = Paths.get(ordersPath);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path filePath : directoryStream) {
                String fileName = filePath.getFileName().toString();
                String fileDate = fileName.substring(7, 15); // Adjust the substring range based on the actual file name format
                Order order = readOrderFromFile(filePath);
                if (order != null) {
                    OrderStamp stamp = new OrderStamp(fileDate, order.getOrderNumber(), order);
                    register.put(fileDate, stamp);

                    int currentOrderNumber = order.getOrderNumber();
                    if (currentOrderNumber > latestOrderNumber) {
                        latestOrderNumber = currentOrderNumber;
                    }
                }
            }
        } catch (IOException e) {
            // Handle any exceptions that occur during file retrieval
            e.printStackTrace();
        }
    }


    /**
     * Reads an Order object from a file.
     * This method reads the contents of the specified file and unpacks the order object
     * stored in the file.
     *
     * @param filePath the path of the file to read the order from
     * @return the Order object read from the file, or null if the file is empty or cannot be parsed
     */
    private Order readOrderFromFile(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Skip the header line
            String orderData = reader.readLine();
            if (orderData != null) {
                String[] orderFields = orderData.split(",");
                // Parse the order fields and create an Order object
                // Set the appropriate field values
                Order order = new Order();
                order.setOrderNumber(Integer.parseInt(orderFields[0]));
                order.setCustomerName(orderFields[1]);
                order.setState(orderFields[2]);
                order.setTaxRate(new BigDecimal(orderFields[3]));
                order.setProductType(orderFields[4]);
                order.setArea(new BigDecimal(orderFields[5]));
                order.setCostPerSquareFoot(new BigDecimal(orderFields[6]));
                order.setLaborCostPerSquareFoot(new BigDecimal(orderFields[7]));
                order.setMaterialCost(new BigDecimal(orderFields[8]));
                order.setLaborCost(new BigDecimal(orderFields[9]));
                order.setTax(new BigDecimal(orderFields[10]));
                order.setTotal(new BigDecimal(orderFields[11]));

                return order;
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * Removes order files for a given date.
     * This method deletes the order files associated with the specified date.
     *
     * @param date the date for which order files should be removed
     * @throws NoOrdersFoundException if no order files are found for the given date
     */
    private void removeOrderFromFile(String date) throws NoOrdersFoundException {
        Path folderPath = Paths.get(ordersPath);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path filePath : directoryStream) {
                String fileName = filePath.getFileName().toString();
                String fileDate = fileName.substring(7, 15); // Adjust the substring range based on the actual file name format
                if (fileDate.equals(date)) {
                    //If the date extracted from the file name matching the
                    //date that was passed it we simply remove the file.
                    Files.delete(filePath);
                }
            }
        } catch (IOException e) {
            // Handle any exceptions that occur during file retrieval
            throw new NoOrdersFoundException("File was not found for that date" + date);
        }
    }


}




