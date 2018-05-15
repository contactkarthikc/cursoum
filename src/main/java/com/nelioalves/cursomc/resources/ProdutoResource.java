package com.nelioalves.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Produto;
import com.nelioalves.cursomc.dto.ProdutoDTO;
import com.nelioalves.cursomc.resources.utils.URL;
import com.nelioalves.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;
	
	// Essa requisição está sendo monitorada pelo Handler (ResourceExceptionHandler.java)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	private ResponseEntity<Produto> find(@PathVariable Integer id) {
		Produto obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	// exemplo de chamada http localhost:8080/categorias/nome?teste&page?linesPerPage=3&page=1&direction=DESC
	@RequestMapping(method=RequestMethod.GET)
	private ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(name="nome", defaultValue="") String nome,
			@RequestParam(name="categorias", defaultValue="") String categorias,
			@RequestParam(name="page", defaultValue="0") Integer page, 
			@RequestParam(name="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(name="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(name="direction", defaultValue="ASC") String direction) {
		
		//o nome pode ter caracteres especiais e nomes em branco por isso, preciso decodificá-lo
		String nomeDecoded = URL.decodeParam(nome);
		
		//como os ids na solicitação GET vem como string e estaremos recebendo separados por virugla 
		//esse método converte a string em uma lista de Integer para ser passada para o findPage
		List<Integer> ids = URL.decodeIntList(categorias);
		
		Page<Produto> list = service.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		// converte para um objeto DTO
		Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj));
		
		return ResponseEntity.ok().body(listDto);
	}
}
