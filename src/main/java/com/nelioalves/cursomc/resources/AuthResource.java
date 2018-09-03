package com.nelioalves.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.dto.EmailDTO;
import com.nelioalves.cursomc.security.JWTUtil;
import com.nelioalves.cursomc.security.UserSS;
import com.nelioalves.cursomc.services.AuthService;
import com.nelioalves.cursomc.services.UserService;

/**
 * Esse resoruce serve para renovar o token do usuário 
 * @author 08215225748
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service;
	
	//o usuário precisa estar logado para chamar o metodo https://localhost:8080/auth/refresh_token
	// passando o token como parâmetro
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		// pega usuário logado
		UserSS user = UserService.authenticated();
		// gera novo token
		String token = jwtUtil.generateToken(user.getUsername());
		// adiciono o token na resposta
		response.addHeader("Authorization", "Bearer " + token);
		// expoe o cabeçalho que será passada uma authorization. Necessário para ser aceito no mecanismo de cors
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	// metodo para que o usuário receva um email com uma nova senha. Caminho https://localhost:8080/auth/forgot/
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDTO) {
		service.sendNewPassword(objDTO.getEmail());
		return ResponseEntity.noContent().build();
	}
}
