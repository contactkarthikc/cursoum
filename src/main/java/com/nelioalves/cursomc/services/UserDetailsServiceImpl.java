package com.nelioalves.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.security.UserSS;

//permite a busca pelo nome username
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private ClienteRepository repo;
	
	// recebe o usuário e retorna o UserDetail, ou seja, busca o usuario pelo seu email (username)
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// executamos uma busca normal por email para retornar o usuário
		Cliente cli = repo.findByEmail(email);
		//nao encontrou o email informado
		if(cli == null) {
			throw new UsernameNotFoundException(email);
		}
		
		// retorno uma instancia de UserSS 
		return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}

}
