package com.nelioalves.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer>{
	
	// findAllBy -> lista todo mundo e ordena
	// OrderByNome - indica o campo a ser ordenado
	@Transactional(readOnly=true)
	public List<Estado> findAllByOrderByNome();
}
