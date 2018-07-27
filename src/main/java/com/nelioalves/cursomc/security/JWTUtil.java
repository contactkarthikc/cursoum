package com.nelioalves.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// @Component indica que a classe pode ser injetada em outras classes como um componente
@Component
public class JWTUtil {

	// recupera o parametro jwt.secret do application.properties
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	// gerar um token
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				// define o tempo de expiração desse token que é a hora atual + o tempo de expiração
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				// passa o algoritimo de autenticação e a palavra chave para embaralhar o token
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	
}
