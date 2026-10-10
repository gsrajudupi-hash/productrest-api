package com.ltm.training.producthive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ltm.training.producthive.model.Product;
/**
 * JPA Repository is mainly used for managing the data in a Spring Boot Application. 
 * JpaRepository is particularly a JPA specific extension for Repository.
 * Jpa Repository contains the APIs for basic CRUD operations, the APIS for 
 * pagination, and the APIs for sorting.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	/* Long is data type of @Id field of Product Class
	 * 
	 * This interface has save(),findAll(),findById(),deleteById(),count()
       etc.. inbuilt methods of jpa repository for various database operations.
       
       This interface will be implemented by class automatically
	 */
	
	/* Customer Queries using- JPQL ********************
	 * 
	 * @Query specifies that you're providing a custom JPQL query.
	 * We use the REPLACE function to remove spaces both from the p.name 
	 * field and from the provided :name, making them both single continuous strings with no spaces.
	 * JPQL query that selects products where the lowercase name 
	 * contains the lowercase input name with wildcards. % - Any no. of characters, _ - Single character
	 * @Param("name") is used to bind the name parameter from the 
	 * method signature to the :name placeholder in the query
	 */
	
	
	@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Product> findProductsByNameContainingIgnoreCase(@Param("name") String name);
}
