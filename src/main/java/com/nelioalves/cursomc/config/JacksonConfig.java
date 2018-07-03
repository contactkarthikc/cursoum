package com.nelioalves.cursomc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelioalves.cursomc.domain.PagamentoComBoleto;
import com.nelioalves.cursomc.domain.PagamentoComCartao;

// a anotação @Configuraçtion indica algo que precisa ser rodado quando a aplicação subir
@Configuration
public class JacksonConfig {
	// https://stackoverflow.com/questions/41452598/overcome-can-not-construct-instance-of-interfaceclass-without-hinting-the-pare

	//metodo padrao do jackson para registrar os tipos de classe. No caso da herança ele precisa se resolver ao receber
	//tipo no formato json. O json precisa instanciar a classe correspondente.
	/* EXEMPLO de json
	{
		"cliente" : {"id" : 1},
		"enderecoDeEntrega" : {"id" : 1},
		"pagamento" : {
			"numeroDeParcelas" : 10,
			"@type": "pagamentoComCartao"
		},
		"itens" : [
			{
				"quantidade" : 2,
				"produto" : {"id" : 3}
			},
			{
				"quantidade" : 1,
				"produto" : {"id" : 1}
			}
		]
		}*/
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
			public void configure(ObjectMapper objectMapper) {
				objectMapper.registerSubtypes(PagamentoComCartao.class);
				objectMapper.registerSubtypes(PagamentoComBoleto.class);
				super.configure(objectMapper);
			}
		};
		return builder;
	}
}