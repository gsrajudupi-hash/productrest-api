package com.ltm.training.producthive.auth.dto;

import java.time.LocalDate;

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
		@NotBlank
		String fname,

		@NotBlank
		String lname,

		@Email
		@NotBlank
		String email,

		@NotBlank
		@Size(min = 8, max = 72)
		String password,

		@NotBlank
		String phoneNo,

		@NotNull
		LocalDate dob

		) {

}
