package com.vincode.flooringmastery.dao;

import com.vincode.flooringmastery.FlooringMasteryApplication;
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
    private final String ORDERS_PATH = "C:\\Users\\verej\\OneDrive\\Documents\\repos\\flooring-mastery\\src\\main\\resources\\orders";
    private final Map<String, OrderStamp> register;
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
    public Order addOrder(String date, Order order) throws InvalidOrderException {

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

        return order;
    }

    @Override
    public Order getOrder(String date, int orderNumber) {
        OrderStamp os = register.get(date);
        if (os != null) {
            Order order = os.getOrderDetail();
            if (order.getOrderNumber() == orderNumber) {
                return order;
            }
        }
        return null;
    }


    @Override
    public Order removeTheOrder(String date, Long orderNumber) {
        return null;
    }

    @Override
    public void export() {

    }

    //Creating a register for all files that will be added for easy access.
    private void registerOrder(String date, Order order) {
        OrderStamp stamp = new OrderStamp(date, order.getOrderNumber(), order);
        register.put(stamp.getDate(), stamp);
    }

    private void writeOrderToFile(Order order, Path filePath) throws InvalidOrderException {

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            writer.println(
                    order.getOrderNumber()+ "," +
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
            throw new InvalidOrderException("File not found! Can't write order to the file!");
        }
    }

    //When the instance will be created will automatically call this method to populate the register with the
    // reference to the orders.
    private void readFolderFiles() {
        Path folderPath = Paths.get(ORDERS_PATH);
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

    //This method will unpack the order object from the file
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
            e.printStackTrace();
        }
        return null;
    }


}




