package com.ltm.training.producthive.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ltm.training.producthive.exception.ResourceNotFoundException;
import com.ltm.training.producthive.model.Dealer;
import com.ltm.training.producthive.model.DealerAndAddressProjection;
import com.ltm.training.producthive.service.DealerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value="/api")
public class DealerController {
	
	private final DealerService dealerService;

	public DealerController(DealerService dealerService) {
		super();
		this.dealerService = dealerService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> createDealer(
	        @Valid @RequestBody Dealer dealer) {

	    var address = dealer.getAddress();

	    if (address == null) {
	        return ResponseEntity.badRequest()
	                .body("Address details are required");
	    }

	    // Establish bidirectional relationship
	    address.setDealer(dealer);
	    dealer.setAddress(address);

	    var registeredDealer =dealerService.registerDealer(dealer);

	    if (registeredDealer != null) {
	        return ResponseEntity.ok("Registration Successful");
	    }

	    return ResponseEntity.badRequest()
	            .body("Registration Failed");
	}
	
	//Method to demonstrate orphanRemoval=true/false
	//identifies the Address resource belonging to that Dealer.
	//http://localhost:8088/producthive/api/1/address
	@DeleteMapping("/dealers/{did}/address")
	public ResponseEntity<String> removeDealerAddress(
	        @PathVariable Long did) throws ResourceNotFoundException {

	    var dealer = dealerService.getDealerById(did)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Dealer Not Found for this Id: " + did));

	    if (dealer.getAddress() == null) {
	        return ResponseEntity.badRequest()
	                .body("Dealer does not have an address");
	    }

	    // Remove Address from Dealer
	    dealer.setAddress(null);

	    // Save Dealer
	    dealerService.registerDealer(dealer);

	    return ResponseEntity.ok(
	            "Address removed successfully. "
	            + "Because orphanRemoval=true, the Address record is deleted from the database.");
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<Boolean> loginDealer(
	        @RequestBody Dealer dealer) throws ResourceNotFoundException {

	    var email = dealer.getEmail();
	    var password = dealer.getPassword();

	    var existingDealer = dealerService.loginDealer(email)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Dealer doesn't exist: " + email));

	    var isLogin = email.equals(existingDealer.getEmail())
	            && password.equals(existingDealer.getPassword());

	    return ResponseEntity.ok(isLogin);
	}
	
	//Open PostMan, make a GET Request - http://localhost:8088/producthive/api/dealers
	
		Logger logger=LoggerFactory.getLogger(DealerController.class);
		
		@GetMapping("/dealers")
		public ResponseEntity<List<DealerAndAddressProjection>> getDealerInfo(){
			try {
				List<DealerAndAddressProjection> selectedFields=dealerService.getDealerInfo();
				logger.info("Dealer Information fetched Successfully");
				return ResponseEntity.ok(selectedFields);
			}catch(Exception e) {
				e.printStackTrace();
				logger.error("Dealer Information fetched UnSuccessfully");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			}
		}
}
