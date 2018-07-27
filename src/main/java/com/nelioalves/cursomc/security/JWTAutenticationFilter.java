package com.nelioalves.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelioalves.cursomc.dto.CredenciaisDTO;

// um filtro captura todas as requisiçẽoes, executa um processamento de validação
// UsernamePasswordAuthenticationFilter esse filtro já vem preparado para interceptar o /login.
// inclusive /login já é reservado para o spring security
public class JWTAutenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTUtil jwtUtil;
	
	// construtor
	public JWTAutenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	// metodo que tenta autenticar
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			// monto credenciaisDTO baseado na requisição que precisa vir com usuário e senha
			CredenciaisDTO creds = new ObjectMapper()
	                .readValue(req.getInputStream(), CredenciaisDTO.class);
	
			// com os dados do usuario e senha, instancio um token do spring securiy, observe a lista de permissões vazia
	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
	        
	        //esse método é responsável por validar o login. baseado nas implementaçẽos feitas dos contratos do sbring
	        Authentication auth = authenticationManager.authenticate(authToken);
	        // retorna a autenticação sendo válida ou nao
	        return auth;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	// implementação caso a autenticação seja um sucesso
	protected void successfultAuthentication(HttpServletRequest req, 
			HttpServletResponse res, 
			FilterChain chain,
			Authentication auth)
			throws IOException, ServletException {
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		// coloco o token no cabeçalho da requisição
		res.addHeader("Authorization", "Bearer " + token);
	}
	
}
