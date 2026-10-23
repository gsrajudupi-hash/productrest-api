package com.ltm.training.producthive.security;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ltm.training.producthive.model.Dealer;
import com.ltm.training.producthive.repository.DealerRepository;

/**
 * Spring Security needs a way to load a registered dealer 
 * from MySQL when authenticating a request.
 * 
 * Here, ROLE_DEALER is assigned to every authenticated dealer. 
 * There is no Admin role or separate Admin entity.
 * This implementation assumes your Dealer entity has getEmail() and getPassword() methods.
 */

@Service
public class DealerUserDetailsService implements UserDetailsService{
	
	 private final DealerRepository dealerRepository;

	public DealerUserDetailsService(DealerRepository dealerRepository) {
		super();
		this.dealerRepository = dealerRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 Dealer dealer = dealerRepository.findByEmail(email)
	                .orElseThrow(() ->
	                    new UsernameNotFoundException(
	                        "Dealer not found: " + email));

	        return User.withUsername(dealer.getEmail())
	                .password(dealer.getPassword())
	                .authorities("ROLE_DEALER")
	                .build();
	}
}
