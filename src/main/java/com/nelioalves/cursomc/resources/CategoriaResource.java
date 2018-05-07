package com.nelioalves.cursomc.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//indica que uma classe rest
@RestController
//indica o nome do serviço rest
@RequestMapping(value="/categorias")
public class CategoriaResource {

	//indico a chamada HTML
	@RequestMapping(method=RequestMethod.GET)
	private String listar() {
		return "Rest está funcionando!";
	}
	
}
