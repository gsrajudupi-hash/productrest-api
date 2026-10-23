package com.ltm.training.producthive.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.core.type.TypeReference;
import com.ltm.training.producthive.dto.OrderSummary;
import com.ltm.training.producthive.model.Order;
import com.ltm.training.producthive.model.OrderDetails;
import com.ltm.training.producthive.repository.OrderRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class OrderService {

	//Constructor DI
	private final OrderRepository orderRepository;

	//Jackson Object Mapper to deserialize JSON String
	private final ObjectMapper objectMapper;

//private int a;

	public Order createOrder(Order order) {
		List<OrderDetails> detailsCopy =new ArrayList<>(order.getOrderDetailsList());

		for(OrderDetails details:detailsCopy) {
			order.addOrderDetails(details); //Set Relationship b/w Order with Orderdetails
		}
		return orderRepository.save(order);
	}


	/*
	 *  Spring Boot provides built-in support for pagination through the use of 
	 *  its Page and Slice interfaces. 
	 *  These interfaces allow you to page through large datasets and provide f
	 *  eatures like page navigation, total item count, and sorting. 
	 *  By using Spring Boot's pagination features, you can efficiently manage 
	 *  and display large amounts of data in your web application, enhancing 
	 *  user experience and improving performance.
	 */
	/*
	 * Pageable is a feature in Spring Boot that allows you to implement 
	 * pagination in your applications. 
	 * It enables you to split large datasets into smaller, more manageable pages,
	 *  improving the performance and usability of your APIs. 
	 *  The Pageable interface is used to define the pagination parameters, 
	 *  such as the page number and size. 
	 *  You can use it in combination with the Page object to retrieve the desired
	 *   data. This feature is particularly useful in applications where you 
	 *   need to handle large amounts of data and provide a user-friendly 
	 *   experience.
	 */
	public Page<Order> getAllOrders(Pageable pageable) {
		Page<Order> orders = orderRepository.findAllWithDetails(pageable);
		return orders;
	}

	@Transactional
	public List<OrderSummary> getOrderSummary(
			BigDecimal minTotal) {

		try {
			String json =
					orderRepository.getOrderSummary(minTotal);

			List<OrderSummary> summaries =
					objectMapper.readValue(
							json == null ? "[]" : json,
									new TypeReference<List<OrderSummary>>() {}
							);

			return summaries.stream()
					.sorted(Comparator.comparing(
							OrderSummary::totalAmount,
							Comparator.reverseOrder()
							))
					.toList();

		} catch (Exception ex) {
			throw new IllegalStateException(
					"Failed to retrieve order summary", ex);
		}
	}
}

