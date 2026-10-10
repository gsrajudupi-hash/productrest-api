package com.ltm.training.producthive.repository;


import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.ltm.training.producthive.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
	//This method returns a Page<Order> where each Order has its OrderDetails eagerly Loaded
	@Query("SELECT DISTINCT ord FROM Order ord LEFT JOIN FETCH ord.orderDetailsList")
	Page<Order> findAllWithDetails(Pageable pageable);
	
	//Call to Stored Procedure with IN & OUT parameters
	@Procedure(name="Order.orderSummary",outputParameterName = "p_result")
	String getOrderSummary(@Param("p_min_total") BigDecimal minTotal);
}
