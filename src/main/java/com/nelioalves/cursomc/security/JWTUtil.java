package com.nelioalves.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// @Component indica que a classe pode ser injetada em outras classes como um componente
@Component
public class JWTUtil {

	// recupera o parametro jwt.secret do application.properties
	@Value("${jwt.secret}")
	private String secret;
	
	
	// recupera o tempo de expiração do application.properties
	@Value("${jwt.expiration}")
	private Long expiration;
	
	// recebe um usuario e gerar um token
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				// define o tempo de expiração desse token que é a hora atual + o tempo de expiração
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				// passa o algoritimo de autenticação e a palavra chave para embaralhar o token
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	public boolean tokenValido(String token) {
		// carrega as reivindicações do token
 		Claims claims = getClaims(token);
 		if (claims != null) {
 			String username = claims.getSubject();
 			Date expirationDate = claims.getExpiration();
 			Date now = new Date(System.currentTimeMillis());
 			//verifico se o token expirou
 			if (username != null && expirationDate != null && now.before(expirationDate)) {
 				return true;
 			}
 		}
 		return false;
 	}
	
	public String getUsername(String token) {
 		Claims claims = getClaims(token);
 		if (claims != null) {
 			return claims.getSubject();
 		}
 		return null;
 	}
	
	private Claims getClaims(String token) {
 		try {
 		// recupera os Claims(reinvindicações) a partir de um token
 			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
 		}
 		// se o tokem for inválido, retorna nulo
 		catch (Exception e) {
 			return null;
 		}
 	}
	
}
