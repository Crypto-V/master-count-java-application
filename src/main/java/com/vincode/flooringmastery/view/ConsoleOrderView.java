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
        io.print("----------------------------");
        io.print("|*   Flooring Program     *|");
        io.print("----------------------------");
        io.print("| 1. Display Orders        |");
        io.print("| 2. Add an Order          |");
        io.print("| 3. Edit an order         |");
        io.print("| 4. Remove an Order       |");
        io.print("| 5. Export All Data       |");
        io.print("| 6. Quit                  |");
        io.print("----------------------------");

        return io.readInt("--> Please select from the above choices.", 1, 6);
    }


    //get user information
    public String getCustomerName() {
        return io.readString("--> Please enter your name: ").toUpperCase();
    }

    public String getState() {
        return io.readString("--> Please enter the state abbreviation: ").toUpperCase();
    }

    public BigDecimal getArea() {
        String areaString = io.readString("--> Please enter the area you want to calculate: ");
        return new BigDecimal(areaString);
    }

    public String getOrderDate() {
        LocalDate orderDate = io.readDate("--> Enter the order date (MM-dd-yyyy): ");
        //formatting the date mm-dd-yyyy
        return io.formatDate(orderDate);
    }

    public String getProductType() {
        io.print("<< Material Type >>");
        io.print("Carpet");
        io.print("Laminate");
        io.print("Tile");
        io.print("Wood");

        return io.readString("--> Please select from the above choices: ").toUpperCase();

    }


    //Display orders section
    public void displayAllOrdersSectionBanner() {
        io.print("----------------------");
        io.print("|   Display Orders   | ");
        io.print("----------------------");
    }

    public String getUserRequestedDate() {
        LocalDate orderDate = io.readDate("--> Enter the order date in this format MM-dd-yyyy or mmddyyyy");
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
        io.print("----------------------");
        io.print("|     Add Order      | ");
        io.print("----------------------");
    }

    public void displayPreOrderBanner(String date, Order order) {
        if (order != null) {
            io.print("<< Order Info >>");
            String shortOrderInfo = String.format("\nOrder Date: %s\nCustomer Name: %s\nOrder Total: %s", date, order.getCustomerName(), order.getTotal());
            io.print(shortOrderInfo);
        }
    }

    public boolean getUserConfirmation() {
        return io.readYesNo("\n--> Would you like to add/update this order? (y/yes)");
    }

    public void displayOrder(Order order) {
        io.print(order.toString() + "\n");
    }


    //Edit order section
    public void displayEditSectionBanner() {
        io.print("----------------------");
        io.print("|     Edit Order     | ");
        io.print("----------------------");
    }

    public int getOrderNumber() {
        return io.readInt("--> Enter the order number: ");
    }

    public String readCustomerName(String currentName) {
        String customerName = io.readString("--> Enter customer name (" + currentName + "): ").toUpperCase();
        // Perform validation logic here if needed
        if (!customerName.isEmpty()) return customerName;
        return currentName;
    }

    public String readState(String currentState) {
        String state = io.readString("--> Please enter the state abbreviation (" + currentState + "): ").toUpperCase();
        // Perform validation logic here if needed
        if (!state.isEmpty()) return state;
        return currentState;
    }

    public BigDecimal readArea(BigDecimal currentArea) {
        String areaStr = io.readString("--> Please enter the area you want to calculate (" + currentArea + "): ");
        // Perform validation logic here to ensure a valid BigDecimal value
        if (!areaStr.isEmpty()) return new BigDecimal(areaStr);
        return currentArea;
    }

    public String readProductType(String currentProductType) {
        io.print("<< Material Type >>");
        io.print("Carpet");
        io.print("Laminate");
        io.print("Tile");
        io.print("Wood");
        String productType = io.readString("--> Select the product type (" + currentProductType + "): ").toUpperCase();
        // Perform validation logic here if needed

        if (!productType.isEmpty()) return productType;
        return currentProductType;
    }


    //Delete section
    public void displayRemoveOrderBanner() {
        io.print("----------------------");
        io.print("|     Remove Order   | ");
        io.print("----------------------");
    }

    public boolean getUserConfirmationToDelete() {
        return io.readYesNo("\n--> Would you like to remove this order? (y/yes)");
    }


    //Export Section
    public void displayExportBanner() {
        io.print("----------------------");
        io.print("|  Export all data   | ");
        io.print("----------------------");
    }

    public void displayExportFinished() {
        io.print("Data exported. Refresh and Check backup folder.");
    }
}
