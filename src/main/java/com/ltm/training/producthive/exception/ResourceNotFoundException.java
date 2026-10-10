package com.ltm.training.producthive.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * ResourceNotFoundException - Custom Exception class
 * It is used to signal when a requested resource (such as a product with a specific ID) 
 * is not available in the database or other data sources.
 * By creating a custom exception, you can provide more meaningful error messages and 
 * handle specific cases of missing resources in a standardized way.
 */

/*
 * @ResponseStatus(value = HttpStatus.NOT_FOUND): 
 * This annotation automatically sets the HTTP status code to 404 (Not Found) when this exception is thrown.
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
