package com.ltm.training.producthive.auth.dto;

/**
 * The expiresIn field represents the token lifetime in milliseconds.
 */
public record AuthResponse(
		String token,
		String tokenType,
		long expiresIn
		) {

}
