package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;
import com.nelioalves.cursomc.services.EmailService;
import com.nelioalves.cursomc.services.MockEmailService;

//a anotação @Configuraçtion indica algo que precisa ser rodado quando a aplicação subir
// classe de configuração que indica que todos os Beans que estiverem dentro dessa classe
// ficaram ativos quando o profile test estiver ativo para o sistema. 
// application.properties -> spring.profiles.active=test
@Configuration
@Profile("test")
public class TesteConfig {

	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		// método responsável por instanciar os objetos e incluí-los na base de testes
		dbService.instantiateTestDatabase();
		return true;
	}
	
	// quando tenho um método com @Bean, esse método fica disponível para ser usado nas classes da aplicação
	// nesse caso, quando dou um @Autowired ele vai injetar a instancia desse método para EmailService
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
