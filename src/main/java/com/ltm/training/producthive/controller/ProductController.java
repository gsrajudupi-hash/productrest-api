package com.ltm.training.producthive.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ltm.training.producthive.exception.ResourceNotFoundException;
import com.ltm.training.producthive.model.Product;
import com.ltm.training.producthive.service.ProductService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


/* Spring RestController annotation is used to create RESTful web services using Spring MVC. 
 * Spring RestController takes care of mapping request data to the defined request handler method. 
 * Once response body is generated from the handler method, it converts it to JSON or XML response.
 *  
 * @RestController indicates that this class handles HTTP requests and automatically 
 * serializes the results to JSON.
 * 
 * @RequestMapping - maps HTTP request with a path to a controller 
 * */
@RestController
@RequestMapping(value = "/api")
public class ProductController {

	//Field Dependency Injection
	@Autowired
	private ProductService productService;

	/*
	 * In Spring Boot, a Response Entity is a class that helps in returning a response to a HTTP request. 
	 * It's part of the Spring Framework and is used to simplify the process of returning HTTP responses. 
	 * 
	 * A ResponseEntity typically includes the HTTP status code, headers, and a body. 
	 * This can be a JSON or XML response, or even a simple string. 
	 * ResponseEntity is a powerful and flexible way to return responses from a Spring Boot application, 
	 * making it easy to manage HTTP communication and handle errors.
	 * 
	 * @RequestBody annotation automatically deserializes the JSON into a Java type
	 * 
	 * @Validated annotation is a tool that helps validate the data being passed to a controller method. 
	 */

	//ResponseEntity - Http Status code, headers, response body
	// http://localhost:8088/producthive/api/products
	@PostMapping("/products")
	public ResponseEntity<Product> saveProduct(@Validated @RequestBody Product product) {

		try {
			Product p=productService.saveProduct(product);
			return ResponseEntity.status(HttpStatus.CREATED).body(p);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}  
	}

	//Open PostMan/Browser, make a GET Request - http://localhost:8088/producthive/api/products
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts(){
		try {
			List<Product> products=productService.listProducts();//Invoke listProducts() service method
			return ResponseEntity.ok(products);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	//Open PostMan/Browser, make a GET Request - http://localhost:8088/producthive/api/products/1004
	//Exception handling is done with Custom Exceptions
	@GetMapping("/products/{pid}")
	public ResponseEntity<Product> getProductById(@PathVariable(value="pid") Long pId) 
			throws ResourceNotFoundException{

		Product p=productService.getSingleProduct(pId).orElseThrow(() ->
		new  ResourceNotFoundException("Product Not Found for this Id : "+pId));

		return ResponseEntity.ok(p);
	}
	
	
	//Open PostMan, make a PUT Request - http://localhost:8088/producthive/api/products/1004
		//Select body -> raw -> JSON 
		//Update JSON product object with new Values.
		/*
		 * Example JSON for PUT (all fields):
		 * {
    			"name": "Ultrabook Laptop 14 inch",
			    "brand": "Lenovo",
			    "madein": "USA",
			    "price": 72999
			}
		 */
	
	@PutMapping("/products/{pid}")  //Full Update of an Object
	public ResponseEntity<Product> updateProduct(
	        @PathVariable Long pid,
	        @Validated @RequestBody Product productRequest) throws ResourceNotFoundException {

	    var product = productService.getSingleProduct(pid)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Product Not Found for this Id: " + pid));

	    // Update product with new values
	    product.setBrand(productRequest.getBrand());
	    product.setMadein(productRequest.getMadein());
	    product.setName(productRequest.getName());
	    product.setPrice(productRequest.getPrice());

	    var updatedProduct = productService.saveProduct(product);

	    return ResponseEntity.ok(updatedProduct);
	}
	
	//Open PostMan, make a PATCH Request - http://localhost:8088/product-hive/api/products/1004
		//Select body -> raw -> JSON
		//Update JSON product object with new Values.
		/*
		 * Example JSON for PATCH (only price and madein):
		 * {
	  			"price": 10000,
	  			"madein": "Germany"
			}
		 */
		//@PatchMapping → used for partial updates (instead of full updates with @PutMapping).

	@PatchMapping("/products/{pid}")
	public ResponseEntity<Product> updateProductPriceAndMadein(
	        @PathVariable Long pid,
	        @Validated @RequestBody Product productRequest) throws ResourceNotFoundException {

	    // Fetch existing product from DB
	    var product = productService.getSingleProduct(pid)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Product Not Found for this Id: " + pid));

	    // Update only price and madein if provided
	    if (productRequest.getPrice() != null &&
	    		productRequest.getPrice().compareTo(BigDecimal.ZERO) >0) {
	        product.setPrice(productRequest.getPrice());
	    }

	    if (productRequest.getMadein() != null) {
	        product.setMadein(productRequest.getMadein());
	    }

	    // Save updated product
	    var updatedProduct = productService.saveProduct(product);

	    return ResponseEntity.ok(updatedProduct);
	}
	
	@DeleteMapping("/products/{pid}")
	public ResponseEntity<Map<String, Boolean>> deleteProduct(
	        @PathVariable Long pid) throws ResourceNotFoundException {

	    // Check whether product exists
		productService.getSingleProduct(pid)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Product Not Found for this Id: " + pid));

	    // Delete product
		productService.deleteProduct(pid);

	    var response =Map.of("Deleted",true);

	    return ResponseEntity.ok(response);
	}
	
	/*
	 * In Spring Boot, RequestParam is a standard annotation used to inject request parameters 
	 * from a web request into a controller method. 
	 * It is often used to extract data from HTTP requests, such as form parameters or query parameters. 
	 * When a controller method is annotated with @RequestParam, the method parameter is 
	 * bound to the value of the corresponding request parameter. 
	 * For example, @RequestParam("name") String name would bind the name method parameter to 
	 * the value of the "name" request parameter.
	 * 	
	 * ResponseEntity<?> indicates that the body of the response can be any type, 
	 * making it a generic reusable class.
	 */
	// GET Request - http://localhost:8088/producthive/api/search?name=Laptop
	
	@GetMapping("/search")
	public ResponseEntity<?> searchProductsByName(
	        @RequestParam String name) {

	    try {
	        var products = productService.searchProductsByName(name);

	        if (products.isEmpty()) {
	            return ResponseEntity
	                    .status(HttpStatus.NOT_FOUND)
	                    .body("No products found with the given name.");
	        }

	        return ResponseEntity.ok(products);

	    } catch (Exception ex) {
	        return ResponseEntity
	                .internalServerError()
	                .body("An error occurred while searching for products.");
	    }
	}
}
