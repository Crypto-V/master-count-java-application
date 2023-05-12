package com.vincode.flooringmastery.controller;

import com.vincode.flooringmastery.exceptions.InvalidOrderException;
import com.vincode.flooringmastery.exceptions.NoOrdersFoundException;
import com.vincode.flooringmastery.model.Order;
import com.vincode.flooringmastery.service.OrderEstimationService;
import com.vincode.flooringmastery.service.OrderService;
import com.vincode.flooringmastery.service.OrderValidationService;
import com.vincode.flooringmastery.view.ConsoleOrderView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


public class OrderController {
    private final OrderValidationService validationService;
    private final OrderEstimationService estimationService;
    private final OrderService orderService;
    private final ConsoleOrderView view;

    public OrderController(OrderValidationService validationService, OrderEstimationService estimationService, OrderService orderService, ConsoleOrderView view) {
        this.validationService = validationService;
        this.estimationService = estimationService;
        this.orderService = orderService;
        this.view = view;
    }

    public void run() throws IOException {
        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1 -> displayOrdersByDate();
                    case 2 -> addOrder();
                    case 3 -> editOrder();
                    case 4 -> System.out.println("Remove an order");
                    case 5 -> keepGoing = false;
                    default -> System.out.println("Default message");
                }

            }
            System.out.println("Good Bye!");
        } catch (InvalidOrderException e) {
            e.printStackTrace();
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

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

    private void addOrder() throws InvalidOrderException, IOException {
        view.addOrderSectionBanner();
        String date = view.getOrderDate();
        String name = view.getCustomerName();
        String state = view.getState();
        BigDecimal area = view.getArea();
        String productType = view.getProductType();

        //Checking if no exception thrown
        try {
            Order order = orderService.createOrder(date, name, state, area, productType);
            simulateLoadingProcess();
            System.out.println("Order Valid!!\n");
            System.out.println("Estimating the cost");
            simulateLoadingProcess();
            view.displayPreOrderBanner(date, order);
            boolean confirmed = view.getUserConfirmation();
            if (confirmed) {
                //Removing dashes from the date string so when added will be in the format of mmddyyyy.
                date = date.replaceAll("-", "");
                orderService.addOrder(date, order);
                System.out.println("Order Confirmed!!");
                view.displayAddedOrder(order);
            } else {
                view.printMenuAndGetSelection();
            }
        } catch (InvalidOrderException e) {
            System.out.println("Invalid: " + e.getMessage());
            System.out.println("!------Let's try again------!");
            addOrder();
            // If the exception was caught, ask the user to enter the order details again.
        }
    }

    private void editOrder() throws InvalidOrderException {
        view.displayEditSectionBanner();
        int orderNumber = view.getOrderNumber();
        String date = view.getOrderDate().replaceAll("-", "");
        Order tempOrder = orderService.getOrder(date, orderNumber);
        view.displayOrderFound(tempOrder);
        Order modifiedOrder = view.modifyOrderInfo(tempOrder);

        // Validate the order
        try {
            orderService.validateOrder(modifiedOrder);
        } catch (InvalidOrderException e) {
            System.out.println("Invalid: " + e.getMessage());
        }

        // Reestimate the order
        Order reestimatedOrder = orderService.reestimateOrder(modifiedOrder);
        boolean confirmed = view.getUserConfirmation();
        if (confirmed) {
            simulateLoadingProcess();
            Order finalOrder = orderService.updateOrder(date, reestimatedOrder);
            simulateLoadingProcess();
            System.out.println("Order Updated!!");
            view.displayOrderFound(finalOrder);
        } else {
            view.printMenuAndGetSelection();
        }
    }





    //This method will simulate the loading process to look more real.
    private void simulateLoadingProcess() {
        System.out.println("Loading");
        for (int i = 0; i < 4; i++) {
            try {
                Thread.sleep(400);
                System.out.print(".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }


//
//    private void removeOrder() {
//        LocalDate date = io.readDate("Enter the order date");
//        int orderNumber = io.readInt("Enter the order number");
//        Order order = orderService.getOrder(date, orderNumber);
//
//        if (order != null) {
//            io.printOrder(order);
//            boolean confirmation = io.readYesNo("Would you like to remove this order?");
//            if (confirmation) {
//                orderService.removeOrder(order);
//                io.print("Order removed.");
//            } else {
//                io.print("Order not removed.");
//            }
//        } else {
//            io.print("Order not found.");
//        }
//    }
//}
}


