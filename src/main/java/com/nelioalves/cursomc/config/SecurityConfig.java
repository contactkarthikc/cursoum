package com.nelioalves.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nelioalves.cursomc.security.JWTAuthenticationFilter;
import com.nelioalves.cursomc.security.JWTAuthorizationFilter;
import com.nelioalves.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
// permite usar em métodos específicos as anotações do tipo @PreAuthorize("hasAnyRole('ADMIN')") que limita o acesso 
// de usuários com determinados perfis
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Autowired
	private JWTUtil jwtUtil;
	
	// Aqui adicionamos a interface e o Spring vai encontrar UserDatailsServiceImpl que foi a implementação feita
	@Autowired
	private UserDetailsService userDetailsService;
	
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-consoles/**"
	};

	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**"
	};
	
	//metodos posts q podem ser acessado mesmo sem estar logado
	private static final String[] PUBLIC_MATCHERS_POST = {
 			"/clientes/**",
 			"/auth/forgot/**"
 	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// se eu estiver com o profile de test ativo, é necessário essa configuração para poder acessar 
		// a url do banco H2 localhost:8080/h2-console
 		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		
		// procura por uma implementacao de cors definida. no caso definimos abaixo
		http.cors()
			.and()
			// desabilita ataques do tipo CSRF q se baseia em guardar o token na session 
			// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/cors/CorsConfiguration.html
			.csrf().disable();
		// garante que não serão criadas sessoes de usuário
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		
		// todos os caminhos de PUBLIC_MATCHERS_GET serão publicos, ou seja, nao precisam de autenticação
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
			//informando HttpMethod.GET apenas o get é publico
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			//para todo o resto autenticação é exigida
			.anyRequest().authenticated();
		
		//registro o filtro responsável pela Autenticação
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		// registro o filtro responsável pela Autorização
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
	//esse método diz para o autenticação do Spring qual é o service capaz de buscar um usuário para login
	// e qual será o passwordEncoder a ser usado
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}



	// permite acesso de origem diferente.
	// https://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/config/annotation/web/builders/HttpSecurity.html
	 @Bean
	  CorsConfigurationSource corsConfigurationSource() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
	    return source;
	  }
	
	 
	 @Bean
	 BCryptPasswordEncoder bCryptPasswordEncoder () {
		 return new BCryptPasswordEncoder();
	 }
	
}
