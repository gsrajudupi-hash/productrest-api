package com.ltm.training.producthive.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ltm.training.producthive.model.Dealer;
import com.ltm.training.producthive.model.DealerAndAddressProjection;
import com.ltm.training.producthive.repository.DealerRepository;

@Service
public class DealerService {
	
	private final DealerRepository dealerRepository;

	public DealerService(DealerRepository dealerRepository) {
		super();
		this.dealerRepository = dealerRepository;
	}
	
	public Dealer registerDealer(Dealer dealer) {
		return dealerRepository.save(dealer); // invokes pre-defined methods of JPA repo
	}
	
	public Optional<Dealer> getDealerById(Long did) {
	    return dealerRepository.findById(did);
	}
	
	public Dealer updateDealer(Dealer dealer) {
	    return dealerRepository.save(dealer);
	}
	
	public Optional<Dealer> loginDealer(String email) {
		return dealerRepository.findByEmail(email); //Invokes custom method of JPA repo
	}

	public List<DealerAndAddressProjection> getDealerInfo() {
		return dealerRepository.findSelectedFieldsFromDealerAndAddress(); //Invokes Custom Query method
	}
}
