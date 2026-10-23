package com.ltm.training.producthive.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productName;
	private Integer quantity;
	private Double price;

	/*
	 * The @ManyToOne annotation establishes the many-to-one relationship with Order.
      fetch = FetchType.LAZY means that the associated Order is loaded on-demand.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_id") //Foreign key
	@JsonBackReference //To backside of relationship for Jack son Serialization
	private Order order;

}
