package com.ltm.training.producthive.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ltm.training.producthive.model.Product;
import com.ltm.training.producthive.repository.ProductRepository;

/*
 * @Service annotation is a stereotype annotation that marks a class as a service layer component. 
 * It's one of the core annotations used to structure your application and enable dependency injection.
 * 
 * The @Service annotation tells Spring that the annotated class contains business logic. 
 * It's typically where you'll implement the core functionality of your application, 
 * such as calculations, data retrieval, or external API interactions.
 */

@Service
public class ProductService {

	private final ProductRepository productRepository;

	//Constructor DI
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product saveProduct(Product p) {
		return productRepository.save(p); //save() is pre-defined method in JPA Repo
	}

	public List<Product> listProducts() {
		return productRepository.findAll(); //findAll() is pre-defined method in JPA Repo
	}

	// Optional return type is to handle Null Pointer Exception
	public Optional<Product> getSingleProduct(long pid) {
		return productRepository.findById(pid);            //Invokes pre-defined method findById() of JPA repository
	}

	public void deleteProduct(long pid) {
		productRepository.deleteById(pid); //Invokes pre-defined method deleteById() of JPA repository
	}
	
	public List<Product> searchProductsByName(String name){
		   return productRepository.findProductsByNameContainingIgnoreCase(name); //Invokes method with custom query
	   }

}
