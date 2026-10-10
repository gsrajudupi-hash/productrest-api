package com.ltm.training.producthive.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ltm.training.producthive.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
