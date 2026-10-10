package com.ltm.training.producthive.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ltm.training.producthive.model.Dealer;
import com.ltm.training.producthive.model.DealerAndAddressProjection;

public interface DealerRepository extends JpaRepository<Dealer, Long>{
	
	//CustomMethod to fetch Object based on email - non id field
	public Optional<Dealer> findByEmail(String email);
	
	public Optional<Dealer> findDistinctByLnameAndPhoneNo(String lname,String phone);
	
	/*
	 * @Query Annotation is used for defining custom queries in Spring Data JPA. 
	 * When you are unable to use the query methods to execute database operations 
	 * then you can use @Query to write a more flexible query to fetch data. 
	 *  
		- @Query Annotation supports both JPQL and native SQL queries.
		- It also supports SpEL expressions.
		- @Param in method arguments to bind query parameter.
		- To define Multiple Joins
	 */
	//Custom Queries - JPQL - Object Oriented Query Language
	@Query("SELECT new com.ltm.training.producthive.model.DealerAndAddressProjection"
			+ "(d.id,d.fname,d.lname,d.phoneNo,"
			+ "d.email,a.street,a.city,a.pincode)"
			+ " FROM Dealer d JOIN d.address a")
	List<DealerAndAddressProjection> findSelectedFieldsFromDealerAndAddress(); 
}
