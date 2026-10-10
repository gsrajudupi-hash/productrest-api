package com.ltm.training.producthive.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ltm.training.producthive.exception.ResourceNotFoundException;
import com.ltm.training.producthive.model.Customer;
import com.ltm.training.producthive.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {
	
	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}
	
	@PostMapping("/customers")
	public ResponseEntity<Customer> saveCustomer(
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("passport") MultipartFile passport,
			@RequestParam("image") MultipartFile image) {
		try {
			Customer customer = new Customer();
			customer.setFirstName(firstName);
			customer.setLastName(lastName);
			customer.setPassport(passport.getBytes());
			customer.setImage(image.getBytes());

			return ResponseEntity.ok(customerService.saveCustomer(customer));
		} catch (IOException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable(value = "id") Long id)
	        throws ResourceNotFoundException {

	    Customer customer = customerService.getCustomerById(id).orElseThrow(() ->
	        new ResourceNotFoundException("Customer not found for this Id : " + id));

	    return ResponseEntity.ok(customer);
	}



	//Open Postman & make GET request - http://localhost:8089/customersoft/api/customers
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		if (customers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(customers);
	}

}
