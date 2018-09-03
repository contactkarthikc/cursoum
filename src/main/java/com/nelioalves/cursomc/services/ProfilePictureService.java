package com.nelioalves.cursomc.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.ProfilePicture;
import com.nelioalves.cursomc.repositories.ProfilePictureRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProfilePictureService {
	
	// injeção de dependência do Spring
	@Autowired
	private ProfilePictureRepository repo;
	
	public ProfilePicture find(Integer id) {
		Optional<ProfilePicture> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + ProfilePicture.class.getName()));
	}
	
	public ProfilePicture findByName(String nome) {
		return repo.findByNome(nome);
	}
	
	public ProfilePicture insert(ProfilePicture obj) {
		return repo.save(obj);
	}

	public ProfilePicture update(ProfilePicture obj) {
		return repo.save(obj);
	}

	public void delete(Integer id) {
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir essa profile.");
		}
	}

	public List<ProfilePicture> findAll() {
		return repo.findAll();
	}

}
