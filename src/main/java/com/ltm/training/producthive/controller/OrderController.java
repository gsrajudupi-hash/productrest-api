package com.ltm.training.producthive.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ltm.training.producthive.dto.OrderSummary;
import com.ltm.training.producthive.model.Order;
import com.ltm.training.producthive.service.OrderService;

@RestController
@RequestMapping(value = "/api")
public class OrderController {
	
	private final OrderService orderService;
	private static final Logger logger=LoggerFactory.getLogger(OrderController.class);

	public OrderController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}
	
	@PostMapping("/orders")
	public ResponseEntity<Order> createOrder(@RequestBody Order order){
		try {
			Order savedOrder=orderService.createOrder(order);
			logger.info("Created order with id: {}",savedOrder.getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("Error Occurred While Creating Order");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	/*
	 * ResponseEntity<Page<Order>>:

	* The endpoint returns a Page of Order objects wrapped in a ResponseEntity, which provides 
	* more control over the HTTP response.
    * @RequestParam Parameters:

	* page: Specifies the page number to retrieve. Default is 0 (first page).
	* size: Specifies the number of records per page. Default is 10.
	* sortBy: Specifies the field by which to sort the records. Default is id.
	* PageRequest.of(page, size, Sort.by(sortBy)):

	* Creates a Pageable object that determines the pagination and sorting strategy for the query.
	* 
	* http://localhost:8088/producthive/api/orders?page=1&size=3&sortBy=id&direction=asc
	* 
	* http://localhost:8088/producthive/api/orders?page=1&size=3&sortBy=id&direction=desc
	 */
	@GetMapping("/orders")
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, 
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction)
	{
		Sort sort = direction.equalsIgnoreCase("desc")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();
	//	 Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> orders = orderService.getAllOrders(pageable);
        logger.info("Fetched orders page: {}, size: {}, sorted by: {}", page, size, sortBy);
        return ResponseEntity.ok(orders);
    }

	
	@GetMapping("/summary")
	public List<OrderSummary> getOrderSummary(
	        @RequestParam(defaultValue = "0.00")
	        BigDecimal minTotal) {

	    return orderService.getOrderSummary(minTotal);
	}
}
