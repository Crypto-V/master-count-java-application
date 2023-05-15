package com.vincode.flooringmastery.controller;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.NoOrdersFoundException;
import com.vincode.flooringmastery.model.Order;
import com.vincode.flooringmastery.service.interfaces.OrderService;
import com.vincode.flooringmastery.view.ConsoleOrderView;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class OrderController {
    private final OrderService orderService;
    private final ConsoleOrderView view;

    public OrderController(OrderService orderService, ConsoleOrderView view) {
        this.orderService = orderService;
        this.view = view;
    }

    /**
     * Runs the order management system.
     *
     * This method continuously displays a menu and prompts the user for their selection.
     * Based on the selected menu option, it calls the corresponding method to perform
     * the desired operation.
     *
     */
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1 -> displayOrdersByDate();
                    case 2 -> addOrder();
                    case 3 -> editOrder();
                    case 4 -> removeOrder();
                    case 5 -> export();
                    case 6 -> keepGoing = false;
                    default -> System.out.println("Hope you love it!");
                }
            }
            System.out.println("Good Bye!");
        } catch (InvalidOrderException e) {
            System.out.println(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }


    /**
     * Displays all orders for a specific date based on user input.
     *
     * It prompts the user to enter a specific date for which they want to see the orders.
     * The method then retrieves the orders for the specified date from the order service
     * and displays them using the view.
     *
     * @throws NoOrdersFoundException if no orders are found for the specified date
     */
    private void displayOrdersByDate() {
        view.displayAllOrdersSectionBanner();
        String date = view.getUserRequestedDate();

        try {
            List<Order> orders = orderService.getOrdersByDate(date);
            view.displayAllOrders(orders);
        } catch (NoOrdersFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Adds an order based on user input.
     * <p>
     * This method prompts the user for information about the order, creates an Order object,
     * and handles the order confirmation process. If the order is valid and confirmed by the user,
     * it is passed to the order service for further processing. As soon as the order was added the user will
     * receive an order confirmation and will be able to view the full order.
     * <p>
     * InvalidOrderException is caught and handled.
     */
    private void addOrder() {
        view.addOrderSectionBanner();
        String date = view.getOrderDate();
        String name = view.getCustomerName();
        String state = view.getState();
        BigDecimal area = view.getArea();
        String productType = view.getProductType();

        try {
            Order order = orderService.createOrder(date, name, state, area, productType);
            simulateLoadingProcess();
            System.out.println("--> Order Valid!!\n");
            System.out.println("--> Estimating the cost");
            simulateLoadingProcess();
            view.displayPreOrderBanner(date, order);
            boolean confirmed = view.getUserConfirmation();

            if (confirmed) {
                date = date.replaceAll("-", "");
                orderService.addOrder(date, order);
                System.out.println("--> Order Confirmed!!");
                view.displayOrder(order);
            } else {
                System.out.println("-| Order Canceled!! |-");
                view.printMenuAndGetSelection();
            }
        } catch (InvalidOrderException e) {
            System.out.println("--! Invalid: " + e.getMessage());
            System.out.println("\n<------Let's try again------>");
            addOrder();
        }
    }


    /**
     * Edits an existing order based on user input.
     * <p>
     * This method prompts the user for the order number and date of the order to be edited.
     * It retrieves the corresponding order from the order service and displays its details.
     * The method then prompts the user to enter new values for the customer name, state,
     * area, and product type of the order. The updated order is validated, and if it is valid
     * and confirmed by the user, the existing order is updated with the new values and estimated.
     *
     * @throws InvalidOrderException if the order or any entered values are invalid
     */
    private void editOrder() throws InvalidOrderException {
        view.displayEditSectionBanner();

        // Reading values for extracting the object
        int orderNumber = view.getOrderNumber();
        String date = view.getOrderDate().replaceAll("-", "");

        try {
            // Extracting the object
            Order tempOrder = orderService.getOrder(date, orderNumber);
            view.displayOrder(tempOrder);

            // Reading the new values
            String name = view.readCustomerName(tempOrder.getCustomerName());
            String state = view.readState(tempOrder.getState());
            BigDecimal area = view.readArea(tempOrder.getArea());
            String productType = view.readProductType(tempOrder.getProductType());

            // Validate the order
            try {
                orderService.validateOrder(name, state, area, productType);
            } catch (InvalidOrderException e) {
                System.out.println("--! Invalid order: " + e.getMessage());
                editOrder();
            }
            view.displayPreOrderBanner(date, tempOrder);

            boolean confirmed = view.getUserConfirmation();
            if (confirmed) {
                // Update existing order and estimate
                try {
                    Order reestimatedOrder = orderService.updateOrder(date, orderNumber, name, state, area, productType);
                    simulateLoadingProcess();
                    System.out.println(" -| Order Updated!! |- ");
                    view.displayOrder(reestimatedOrder);
                } catch (NoOrdersFoundException e) {
                    System.out.println("--! No order found: " + e.getMessage());
                } catch (InvalidOrderException e) {
                    System.out.println("--! Invalid order: " + e.getMessage());
                }
            } else {
                System.out.println("-| Order Canceled!! |-");
            }
        } catch (NoOrdersFoundException | InvalidOrderException e) {
            System.out.println("--! Invalid: " + e.getMessage());
        }
    }


    /**
     * Removes an existing order based on user input.
     * <p>
     * This method prompts the user for the order number and date of the order to be removed.
     * It retrieves the corresponding order from the order service and displays its details.
     * The method then prompts the user to confirm the removal of the order.
     * If confirmed, the order is removed from the order service and the removed order is displayed.
     * <p>
     * NoOrdersFoundException and InvalidOrderException will be caught and handled to make sure system will not crash.
     */
    private void removeOrder() {

        view.displayRemoveOrderBanner();

        // Reading values for extracting the object
        int orderNumber = view.getOrderNumber();
        String date = view.getOrderDate().replaceAll("-", "");

        try {
            // Extracting the object
            Order tempOrder = orderService.getOrder(date, orderNumber);
            view.displayOrder(tempOrder);

            boolean confirmed = view.getUserConfirmationToDelete();
            if (confirmed) {
                // Removing order!
                Order removedOrder = orderService.removeOrder(date, orderNumber);
                view.displayOrder(removedOrder);
                simulateLoadingProcess();
                System.out.println(" -| Order Removed!! |- ");
            } else {
                System.out.println("-| Order was not removed!! |-");
            }

        } catch (NoOrdersFoundException | InvalidOrderException e) {
            System.out.println("--! Invalid: " + e.getMessage());
        }


    }


    /**
     * Exports all orders to a file.
     * <p>
     * This method displays a banner indicating the export process has started and simulates
     * a loading process. It then attempts to export all orders using the order service,
     * specifying the current date as the export date. If the export is successful, a message
     * indicating the completion of the export is displayed. If IOException | InvalidOrderException occurs,
     * an appropriate error message is printed.
     */
    private void export() {
        view.displayExportBanner();
        simulateLoadingProcess();
        try {
            orderService.exportAll(LocalDate.now());
        } catch (IOException | InvalidOrderException e) {
            System.out.println("--! Failed to export orders: " + e.getMessage());
        }
        view.displayExportFinished();
    }


    /**
     * Simulates the loading process to create a real feeling of the system retrieving and updating the data.
     */
    private void simulateLoadingProcess() {
        System.out.print("\nLoading");
        for (int i = 0; i < 6; i++) {
            try {
                Thread.sleep(500);
                System.out.print(" #");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

}


