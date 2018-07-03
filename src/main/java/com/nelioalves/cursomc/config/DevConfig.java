package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;

//a anotação @Configuraçtion indica algo que precisa ser rodado quando a aplicação subir
// classe de configuração que indica que todos os Beans que estiverem dentro dessa classe
// ficaram ativos quando o profile test estiver ativo para o sistema. 
// application.properties -> spring.profiles.active=test
@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;
	
	//recupera uma propriedade descrita no progile ativo definido em application.properties
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		//se a estrategia nao for create carrego a base de dados
		if(!"create".equals(strategy)) {
			// método responsável por instanciar os objetos e incluí-los na base de testes
			dbService.instantiateTestDatabase();
		}
		return true;
	}
}
