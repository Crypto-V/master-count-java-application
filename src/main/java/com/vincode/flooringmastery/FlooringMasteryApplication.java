package com.vincode.flooringmastery;

import com.vincode.flooringmastery.controller.OrderController;
import com.vincode.flooringmastery.dao.OrderDaoImpl;
import com.vincode.flooringmastery.dao.ProductDaoImpl;
import com.vincode.flooringmastery.dao.TaxRateDaoImpl;
import com.vincode.flooringmastery.dao.interfaces.OrderDao;
import com.vincode.flooringmastery.dao.interfaces.ProductDao;
import com.vincode.flooringmastery.dao.interfaces.TaxRateDao;
import com.vincode.flooringmastery.service.EstimationServiceImpl;
import com.vincode.flooringmastery.service.OrderServiceImpl;
import com.vincode.flooringmastery.service.ValidationServiceImpl;
import com.vincode.flooringmastery.service.interfaces.EstimationService;
import com.vincode.flooringmastery.service.interfaces.OrderService;
import com.vincode.flooringmastery.service.interfaces.ValidationService;
import com.vincode.flooringmastery.ui.UserIO;
import com.vincode.flooringmastery.ui.UserIOConsoleImpl;
import com.vincode.flooringmastery.view.ConsoleOrderView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlooringMasteryApplication {

	public static void main(String[] args)   {
		SpringApplication.run(FlooringMasteryApplication.class, args);
		UserIO io = new UserIOConsoleImpl();
		ProductDao productDao = new ProductDaoImpl();
		TaxRateDao taxRateDao = new TaxRateDaoImpl();
		OrderDao orderDao = new OrderDaoImpl();
		EstimationService estimationService = new EstimationServiceImpl(productDao,taxRateDao);
		ValidationService orderValidationService = new ValidationServiceImpl(productDao,taxRateDao);
		OrderService orderService = new OrderServiceImpl(orderDao,orderValidationService,estimationService);
		ConsoleOrderView view = new ConsoleOrderView(io);
		OrderController controller = new OrderController(orderService,view);

		controller.run();
	}

}
