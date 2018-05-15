package com.nelioalves.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

	//REFERENCIA: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	//Pesquisa no padrão de nomes do Spring Data
	//findDistinctByNome - busca com distinct pelo parametro nome
	//Containing - referece ao like ex.: LIKE %:nome%
	//And - coloca mais um item na clausula where
	//CategoriasIn - semelhate a IN :categorias
	@Transactional(readOnly=true)
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);

	
	
	//OLD o método findDistinctByNomeContainingAndCategoriasIn faz a mesa coisa que o serach
	
	// dessa forma nao é preciso implementar esse metodo da interface. O Spring gera automaticamente
	// a implementação desse método.
	// @Param("nome") - é feita a associação entre o parâmeto (:nome) e o atributo passado String nome
	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM  Produto obj "
			+ "INNER JOIN obj.categorias cat "
			+ "WHERE obj.nome LIKE %:nome% AND  cat IN :categorias")
	Page<Produto> search(@Param("nome")String nome, @Param("categorias")List<Categoria> categorias, Pageable pageRequest);
}
