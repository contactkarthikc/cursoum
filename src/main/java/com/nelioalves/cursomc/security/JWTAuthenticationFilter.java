package com.nelioalves.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelioalves.cursomc.dto.CredenciaisDTO;

// um filtro captura todas as requisiçẽoes, executa um processamento de validação
// UsernamePasswordAuthenticationFilter esse filtro já vem preparado para interceptar o /login.
// inclusive http://meusite/login já é reservado para o spring security
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTUtil jwtUtil;
	
	// construtor recebe o authenticationManager e o jwtUtil
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
	  	setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
	    this.authenticationManager = authenticationManager;
	    this.jwtUtil = jwtUtil;
	}

	@Override
	// metodo que tenta autenticar
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			// monto credenciaisDTO baseado na requisição que precisa vir com usuário e senha
			// ou seja. tenta pegar os dados que vieram na req e tentando instanciar CrendenciaisDTO
			CredenciaisDTO creds = new ObjectMapper()
	                .readValue(req.getInputStream(), CredenciaisDTO.class);
	
			// com os dados do usuario e senha, instancio um token do spring securiy, observe a lista de permissões vazia
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
	        
	        //esse método é responsável por validar o login. baseado nas implementaçẽos feitas dos contratos do sbring
	        // acessa as classes que implementados as interfaces do spring ex.: UserDetailsServiceImpl
			Authentication auth = authenticationManager.authenticate(authToken);
	        // retorna a autenticação sendo válida ou nao
	        return auth;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	// implementação caso a autenticação seja um sucesso
	// basicamente devo gerar um token e retornar no cabeçalho da requisição
	@Override
	protected void successfulAuthentication(HttpServletRequest req, 
			HttpServletResponse res, 
			FilterChain chain,
			Authentication auth)
			throws IOException, ServletException {
		// auth.getPrincipal() retornao o usuario do Spring Security
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		// coloco o token no cabeçalho da requisição
		//deve serguir esse padrao
		res.addHeader("Authorization", "Bearer " + token);
	}
	
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		 
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json"); 
            response.getWriter().append(json());
        }
        
        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\"}";
        }
    }
	
}
