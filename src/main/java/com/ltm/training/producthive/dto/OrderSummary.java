package com.ltm.training.producthive.dto;

import java.math.BigDecimal;

public record OrderSummary(
		 	Long orderId,
		    String orderNumber,
		    Long totalItems,
		    Long totalQuantity,
		    BigDecimal totalAmount,
		    String orderCategory		
		){}
