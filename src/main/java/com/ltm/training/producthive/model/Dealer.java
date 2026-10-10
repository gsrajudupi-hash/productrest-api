package com.ltm.training.producthive.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
/**
 * Model class for Registration of Users Personal fields 
* 		with 1-1 Bi- Directional Mapping with Address class
 */

@Entity
@Table(name="dealers")
@Data
public class Dealer {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "did")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String fname;

    @Column(name = "last_name", nullable = false)
    private String lname;

    @NotBlank(message = "Password cannot be blank")
    @Size(
        min = 8,
        max = 255,
        message = "Password must be between 8 and 255 characters"
    )
    @Column(nullable = false)
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dob;

    @Column(name = "phone", unique = true, nullable = false)
    private String phoneNo;

    /*Bidirectional 1-1 Relationship
     * Dealer -> Address*/
    //orphanRemoval - Deletes a child enity when u remove it from parents collection
    @OneToOne(mappedBy = "dealer",cascade=CascadeType.ALL,orphanRemoval = true)
    private Address address;
    
    
    /**
     *   Dealer --> Address
     *   
     *   101 - John			1 Mg Road 101
     *   
     *   Dealer dealer=dRepo.findByID(101);
     *   dealer.setAddress(null);
     *   dRepo.save(dealer);
     *   
     */

    
}
