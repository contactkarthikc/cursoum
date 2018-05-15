package com.nelioalves.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods
	// forma simplificada de criar uma um método no Spring
		//finByNOME_DO_CAMPO
		@Transactional(readOnly=true)
		Cliente findByEmail(String email);
}
