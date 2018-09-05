package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;
	
	// Essa requisição está sendo monitorada pelo Handler (ResourceExceptionHandler.java)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		Cliente obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	// exemplo de chamada http://localhost:8080/clientes/email?value=paulo.fernandes@yahoo.com.br
	@RequestMapping(value="/email", method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@RequestParam(value="value") String email) {
		Cliente obj = service.findByEmail(email);
		return ResponseEntity.ok().body(obj);
	}
	
	// ResponseEntity<Void> indica que minha resposta terá o corpo vazio
	// @RequestBody - faz o objeto Json ser convertido automaticamente para o obj Java
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDto){
		Cliente obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		// é uma boa prática retornar a URI do recurso inserido
		// recupera a uri corrente e acrescenta o id do recurso inserido
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
		Cliente obj = service.fromDTO(objDto);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	// indica que apenas usuarios com a role admin podem inserir uma categoria
	// para q funcione, é preciso habilitar esse controle no SecurityConfig.java
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	// indica que apenas usuarios com a role admin podem inserir uma categoria
	// para q funcione, é preciso habilitar esse controle no SecurityConfig.java
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {
		List<Cliente> list = service.findAll();
		// converte para um objeto DTO
		List<ClienteDTO> listDto = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listDto);
	}
	
	// indica que apenas usuarios com a role admin podem inserir uma categoria
	// para q funcione, é preciso habilitar esse controle no SecurityConfig.java
	@PreAuthorize("hasAnyRole('ADMIN')")
	// exemplo de chamada http localhost:8080/categorias/page?linesPerPage=3&page=1&direction=DESC
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(name="page", defaultValue="0") Integer page, 
			@RequestParam(name="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(name="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(name="direction", defaultValue="ASC") String direction) {
		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);
		// converte para um objeto DTO
		Page<ClienteDTO> listDto = list.map(obj -> new ClienteDTO(obj));
		
		return ResponseEntity.ok().body(listDto);
	}
	
	// Fiz uma gambiarra para simular o uso do amazon AWS s3
	// nesse exemplo gravo as imagens no banco h2
	// para enviar uma imagem: endereço (post) http://localhost:8080/clientes/picture
	// no body: retirar Content-Type; Alterar para "form-data", informar a chave "file" (igual ao name desse metodo)
	// e selecionar o arquivo a ser gravado.
	@RequestMapping(value="/picture", method=RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name="file")  MultipartFile file){
		URI uri = service.uploadProfilePicture(file);
		// semelhante ao retorno de um crud
		// renorna a URI para acessar a imagem salva
		return ResponseEntity.created(uri).build();
	}
	
}
