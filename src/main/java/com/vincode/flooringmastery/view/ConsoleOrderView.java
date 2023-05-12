package com.vincode.flooringmastery.view;

import com.vincode.flooringmastery.model.Order;
import com.vincode.flooringmastery.ui.UserIO;

import java.math.BigDecimal;
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
        return io.readString("Please enter your name").toUpperCase();
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

        return io.readString("Please select from the above choices.").toUpperCase();

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
    public void displayEditSectionBanner() {
        System.out.println(" << Edit Order >> ");
    }

    public int getOrderNumber() {
        return io.readInt("Enter the order number: ");
    }

    public Order modifyOrderInfo(Order order) {
        String customerName = io.readString("Enter customer name (" + order.getCustomerName() + ") :").toUpperCase();
        String state = io.readString("Please enter the state abbreviation (" + order.getState() + ") : ").toUpperCase();
        String area = io.readString("Please enter the area you want to calculate (" + order.getArea() + ") : ");
        io.print("<<Material Type>>");
        io.print("Carpet");
        io.print("Laminate");
        io.print("Tile");
        io.print("Wood");
        String productType = io.readString("Select the product type (" + order.getProductType() + ") : ").toUpperCase();

        if (!customerName.isEmpty()) {
            order.setCustomerName(customerName);
        }
        if (!state.isEmpty()) {
            order.setState(state);
        }
        if (!area.isEmpty()) {
            order.setArea(new BigDecimal(area));
        }
        if (!productType.isEmpty()) {
            order.setProductType(productType);
        }
        return order;

    }

    public void displayOrderFound(Order order) {
        io.print(order.toString());
    }

    //Delete section
}
