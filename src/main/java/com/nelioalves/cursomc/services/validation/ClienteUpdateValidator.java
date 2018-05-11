package com.nelioalves.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.resources.exceptions.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repo;
	
	
	// aqui pode ser colocada alguma programação de inicialização
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	//método responsável por fazer a validação e deve retornar true ou false
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		// recupera a URI informada no navegador
		Map<String, String> map = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		// inclua os teste e inserindo erros na lista

		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux!=null && !aux.getId().equals(uriId))
			list.add(new FieldMessage("email", "Email já existe"));
		
		// percorre a lista de field message e joga as mensagens para o contexto de mensagem do framework (mensagens de erro do frame)
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}