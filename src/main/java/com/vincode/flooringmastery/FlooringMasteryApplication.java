package com.vincode.flooringmastery;

import com.vincode.flooringmastery.controller.OrderController;
import com.vincode.flooringmastery.dao.*;
import com.vincode.flooringmastery.service.OrderEstimationService;
import com.vincode.flooringmastery.service.OrderService;
import com.vincode.flooringmastery.service.OrderValidationService;
import com.vincode.flooringmastery.ui.UserIO;
import com.vincode.flooringmastery.ui.UserIOConsoleImpl;
import com.vincode.flooringmastery.view.ConsoleOrderView;

import java.io.IOException;

public class FlooringMasteryApplication {

	public static void main(String[] args) throws IOException {

		UserIO io = new UserIOConsoleImpl();
		ProductDao productDao = new ProductDaoImpl();
		TaxRateDao taxRateDao = new TaxRateDaoImpl();
		OrderDao orderDao = new OrderDaoImpl();
		OrderEstimationService estimationService = new OrderEstimationService(productDao,taxRateDao);
		OrderValidationService orderValidationService = new OrderValidationService(productDao,taxRateDao);
		OrderService orderService = new OrderService(orderDao,orderValidationService,estimationService);
		ConsoleOrderView view = new ConsoleOrderView(io);
		OrderController controller = new OrderController(orderValidationService,estimationService,orderService,view);

		controller.run();
	}

}
