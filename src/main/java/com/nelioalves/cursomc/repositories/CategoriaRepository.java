package com.nelioalves.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nelioalves.cursomc.domain.Categoria;

// interface responsável por acessar o banco de dados. Passamos a classe e o tipo do identificador (Integer, no caso)
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
	
}
