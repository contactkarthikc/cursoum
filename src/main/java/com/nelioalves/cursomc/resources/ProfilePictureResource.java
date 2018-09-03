package com.nelioalves.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.ProfilePicture;
import com.nelioalves.cursomc.services.ProfilePictureService;

/**
 *	Esse resource é uma solução paleativa que tenta simular o serviço do Amazon S3.
 *
 * A gravação das imagens é feita por clientes
 */
@RestController
@RequestMapping(value = "/picture")
public class ProfilePictureResource {

	@Autowired
	private ProfilePictureService service;
	
	// indica o tipo de retorno, desta forma o navegador consegue renderizar a imagem
	@GetMapping(
			value = "/{id}",
			produces = MediaType.IMAGE_JPEG_VALUE
	)
	//@ResponseBody
	//@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<byte[]> find(@PathVariable Integer id) {
		HttpHeaders headers = new HttpHeaders();
	    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		
		ProfilePicture obj = service.find(id);
	    return new ResponseEntity<>(obj.getArquivo(), headers, HttpStatus.OK);
	}
}
