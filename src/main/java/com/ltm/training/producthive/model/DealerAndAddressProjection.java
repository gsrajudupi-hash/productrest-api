package com.ltm.training.producthive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealerAndAddressProjection {
	
	private Long id;
	private String fname;
	private String lname;
	private String phoneNo;
	private String email;
	private String street;
	private  String city;
	private  int pincode;

}
