package com.ltm.training.producthive.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for storing Orders.
 * Implement Collection Mapping & One-Many Association Mapping
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "orders")
//Stored Procedure Configuration
@NamedStoredProcedureQuery(
		name = "Order.orderSummary",
	    procedureName = "sp_order_summary",
	    parameters = {
	        @StoredProcedureParameter(
	            mode = ParameterMode.IN,
	            name = "p_min_total",
	            type = BigDecimal.class
	        ),
	        @StoredProcedureParameter(
	            mode = ParameterMode.OUT,
	            name = "p_result",
	            type = String.class
	        )
	    }
		)
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String orderNumber;

	/*
	 * We have a List<OrderDetails> named orderDetailsList representing the collection of order details 
	 * associated with an order.
		The @OneToMany annotation establishes a one-to-many relationship between Order and OrderDetails.
		mappedBy = "order" indicates that the order field in OrderDetails owns the relationship.
		cascade = CascadeType.ALL ensures that all operations (persist, merge, remove, refresh, detach) 
		are cascaded to the associated OrderDetails.
		orphanRemoval = true automatically removes OrderDetails entities that are no longer referenced 
		in the orderDetailsList.

	 */
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
	@JsonManagedReference //To identify forward side of relationship for JSON Serialization
	private List<OrderDetails> orderDetailsList=new ArrayList<>(); //Collection Mapping

	// Helper methods addOrderDetails and removeOrderDetails are provided to manage the bidirectional relationship conveniently.
    public void addOrderDetails(OrderDetails orderDetails) {
        orderDetailsList.add(orderDetails);
        orderDetails.setOrder(this); //Set relationship here
    }

    public void removeOrderDetails(OrderDetails orderDetails) {
        orderDetailsList.remove(orderDetails);
        orderDetails.setOrder(null);
    }
}
