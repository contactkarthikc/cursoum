package com.nelioalves.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		// necessário para validar se o token é válido, para isso, será necessário extrair os dados do ususário,
		// buscar no banco o usuário e verificar se o mesmo realmente existe
		this.userDetailsService = userDetailsService;
	}

	//responável por filtar e validar as requisições 
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
					throws IOException, ServletException {
		//recupero o token passado
		String header = request.getHeader("Authorization");
		if(header != null && header.startsWith("Bearer")) {
			// getAuthentication(header.substring(7)) - passo o token, no caso, retiro a palavra Bearer da string do header
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			
			if(auth != null) {
				// libera acesso no filtro
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		// libera acesso a página solicitada
		chain.doFilter(request, response);
	}

	//retorna um token válido
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		//verifico se o token é valido
		if(jwtUtil.tokenValido(token)){
			// extraio do token o usuário
			String username = jwtUtil.getUserName(token);
			// recupero o usário do banco
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
	
}
