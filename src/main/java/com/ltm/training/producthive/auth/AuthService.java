package com.ltm.training.producthive.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ltm.training.producthive.auth.dto.AuthResponse;
import com.ltm.training.producthive.auth.dto.LoginRequest;
import com.ltm.training.producthive.auth.dto.RegisterRequest;
import com.ltm.training.producthive.model.Address;
import com.ltm.training.producthive.model.Dealer;
import com.ltm.training.producthive.repository.DealerRepository;
import com.ltm.training.producthive.security.JwtService;

@Service
public class AuthService {

	private final DealerRepository dealerRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final long expirationMs;

	public AuthService(
			DealerRepository dealerRepository,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager,
			JwtService jwtService,
			@Value("${app.jwt.expiration-ms}") long expirationMs) {

		this.dealerRepository = dealerRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.expirationMs = expirationMs;
	}

//	@Transactional
//	public AuthResponse register(RegisterRequest request) {
//
//		if (dealerRepository.existsByEmail(request.email())) {
//			throw new ResponseStatusException(
//					HttpStatus.CONFLICT,
//					"Email is already registered");
//		}
//
//		Dealer dealer = new Dealer();
//
//		dealer.setFname(request.fname());
//		dealer.setLname(request.lname());
//		dealer.setEmail(request.email());
//		dealer.setPhoneNo(request.phoneNo());
//		dealer.setDob(request.dob());
//
//		// Hash the password before storing it.
//		dealer.setPassword(
//				passwordEncoder.encode(request.password()));
//
//		dealerRepository.save(dealer);
//
//		String token = jwtService.generateToken(dealer.getEmail());
//
//		return new AuthResponse(
//				token, "Bearer", expirationMs);
//	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {

	    if (dealerRepository.existsByEmail(request.email())) {
	        throw new ResponseStatusException(
	            HttpStatus.CONFLICT,
	            "Email is already registered"
	        );
	    }

	    Dealer dealer = new Dealer();

	    dealer.setFname(request.fname());
	    dealer.setLname(request.lname());
	    dealer.setEmail(request.email());
	    dealer.setPhoneNo(request.phoneNo());
	    dealer.setDob(request.dob());

	    dealer.setPassword(
	        passwordEncoder.encode(request.password())
	    );

	    Address address = new Address();

	    address.setStreet(request.address().street());
	    address.setCity(request.address().city());
	    address.setPincode(request.address().pincode());

	    // Set both sides of the bidirectional relationship.
	    address.setDealer(dealer);
	    dealer.setAddress(address);

	    // CascadeType.ALL saves the associated address.
	    dealerRepository.save(dealer);

	    String token = jwtService.generateToken(dealer.getEmail());

	    return new AuthResponse(token, "Bearer", expirationMs);
	}
	
	public AuthResponse login(LoginRequest request) {

		Authentication authentication =
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(
								request.email(),
								request.password()));

		String email = authentication.getName();
		String token = jwtService.generateToken(email);

		return new AuthResponse(token, "Bearer", expirationMs);

	}
}
