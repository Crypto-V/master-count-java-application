package com.vincode.flooringmastery.view;

import com.vincode.flooringmastery.model.Order;
import com.vincode.flooringmastery.ui.UserIO;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.List;

public class ConsoleOrderView {

    private final UserIO io;

    public ConsoleOrderView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an order");
        io.print("4. Remove an Order");
        io.print("4. Export All Data");
        io.print("5. Quit");

        return io.readInt("Please select from the above choices.", 1, 5);
    }


    //get user information
    public String getCustomerName() {
        return io.readString("Please enter your name");
    }

    public String getState() {
        return io.readString("Please enter the state abbreviation").toUpperCase();
    }

    public BigDecimal getArea() {
        String areaString = io.readString("Please enter the area you want to calculate");
        return new BigDecimal(areaString);
    }

    public String getOrderDate() {
        LocalDate orderDate = io.readDate("Enter the order date in this format MM-dd-yyyy");
        //formatting the date mm-dd-yyyy
        return io.formatDate(orderDate);
    }

    public String getProductType() {
        io.print("<<Material Type>>");
        io.print("Carpet");
        io.print("Laminate");
        io.print("Tile");
        io.print("Wood");

        return io.readString("Please select from the above choices.");

    }

    //Display orders section
    public void displayAllOrdersSectionBanner() {
        System.out.println(" << Display Orders >> ");
    }

    public String getUserRequestedDate() {
        LocalDate orderDate = io.readDate("Enter the order date in this format MM-dd-yyyy or mmddyyyy");
        //formatting the date mmddyyyy
        return io.formatDate(orderDate).replaceAll("-", "");
    }

    public void displayAllOrders(List<Order> orders) {

        for (Order order : orders) {
            System.out.println(order);
        }


    }

    //Add order section
    public void addOrderSectionBanner() {
        io.print(" << Add order >> ");
    }

    public void displayPreOrderBanner(String date, Order order) {
        if (order != null) {
            io.print("<< Order Info >>");
            String shortOrderInfo = String.format("Order Date: %s\nCustomer Name: %s\nOrder Total: %s", date, order.getCustomerName(), order.getTotal());
            io.print(shortOrderInfo);
        }
    }

    public boolean getUserConfirmation() {
        return io.readYesNo("Would you like to add/update this order?");
    }

    public void displayAddedOrder(Order order) {
        io.print(order.toString());
    }


    //Edit order section
    public int getOrderNumber() {
        return io.readInt("Enter the order number: ");
    }

    public void displayIfOrderExists(Order order) {
        if (order != null) {
            modifyOrder(order);

        } else {
            io.print("This order doesnt exist. ");
        }
    }

    public Order modifyOrder(Order order) {
        String customerName = io.readString("Enter customer name (" + order.getCustomerName() + ") :");
        String state = io.readString("Please enter the state abbreviation (" + order.getState() + ") : ");
        BigDecimal area = new BigDecimal(io.readString("Please enter the area you want to calculate (" + order.getArea() + ") : "));
        String productType = io.readString("Enter the product type (" + order.getProductType() + ") : ");
        productType = getProductType();

        order.setCustomerName(customerName);
        order.setState(state);
        order.setArea(area);
        order.setProductType(productType);

        return order;
    }


}
