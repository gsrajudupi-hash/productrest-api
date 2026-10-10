package com.ltm.training.producthive.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ltm.training.producthive.model.OrderDetails;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

}
