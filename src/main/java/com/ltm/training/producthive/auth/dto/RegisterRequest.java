package com.ltm.training.producthive.auth.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/**
 * The maximum password length here is 72 characters because BCrypt processes at most 72 bytes. 
 * For predictable behavior, restrict passwords to ASCII characters or implement a 
 * carefully designed password-length policy that accounts for UTF-8 byte length.
 */
public record RegisterRequest(
		 @NotBlank(message = "First name cannot be blank")
		    String fname,

		    @NotBlank(message = "Last name cannot be blank")
		    String lname,

		    @Email(message = "Enter a valid email address")
		    @NotBlank(message = "Email cannot be blank")
		    String email,

		    @NotBlank(message = "Password cannot be blank")
		    @Size(min = 8, max = 72,
		          message = "Password must be between 8 and 72 characters")
		    String password,

		    @NotBlank(message = "Phone number cannot be blank")
		    String phoneNo,

		    @NotNull(message = "Date of birth is required")
		    LocalDate dob,

		    @NotNull(message = "Address is required")
		    @Valid
		    AddressRequest address


		) {

}
