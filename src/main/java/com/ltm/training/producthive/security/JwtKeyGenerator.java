package com.ltm.training.producthive.security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.jsonwebtoken.io.Encoders;

public class JwtKeyGenerator {

	public static void main(String[] args) throws Exception {
		
		KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
		generator.init(256);

		SecretKey key = generator.generateKey();

		System.out.println(Encoders.BASE64.encode(key.getEncoded()));
	}
}
