package com.ltm.training.producthive.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Model class for Managing products using Hibernate -JPA
 * 
 * The @Entity annotation specifies that the class is an entity and is 
 * mapped to a database table.
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "prod_seq")
	@SequenceGenerator(name = "prod_seq",initialValue = 1000,allocationSize = 1)
	private Long pid;
	
	@Column(name="product_name",nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String brand;
	
	@Column(nullable = false,length = 50)
	private String madein;
	
	@Column(nullable = false,precision = 10,scale = 2)
	private BigDecimal price;
	
}
