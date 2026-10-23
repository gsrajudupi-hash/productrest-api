package com.ltm.training.producthive.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddressRequest(
		@NotBlank(message = "Street cannot be blank")
	    String street,

	    @NotBlank(message = "City cannot be blank")
	    String city,

	    @NotNull(message = "Pincode is required")
	    @Positive(message = "Pincode must be positive")
	    Integer pincode
	    ) {

}
