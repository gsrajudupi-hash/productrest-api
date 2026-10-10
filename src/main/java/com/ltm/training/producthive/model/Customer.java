package com.ltm.training.producthive.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class Customer {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String firstName;

    private String lastName;

    /*
     * @Lob annotation is used to specify that a persistent property or field 
     * should be persisted as a large object to a database-supported large object type.
     * Like BLOB (Binary Large Object) for byte arrays and CLOB (Character Large Object) for character data.
     * It is used to handle large data that cannot be stored in a regular column, 
     * such as images, documents, or large text.
     * 
     */
    @Lob
    @Column(name = "passport", columnDefinition="LONGBLOB")
    private byte[] passport;

    @Lob
    @Column(name = "image", columnDefinition="LONGBLOB")
    private byte[] image;

}
