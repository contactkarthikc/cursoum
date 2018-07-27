package com.nelioalves.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nelioalves.cursomc.domain.enums.Perfil;

public class UserSS implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String email;
	private String senha;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {
		
	}
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	public Integer getId() {
		return id;
	}

	@Override
	public boolean isAccountNonExpired() {
		// indica se a conta está expirada, pode existir uma logica para tratar isso
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// indica se a conta está bloqueada, pode existir uma logica para tratar isso
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// indica se a credencial está expirada, pode existir uma logica para tratar isso
		return true;
	}

	@Override
	public boolean isEnabled() {
		// indica se o usuário está ativo, pode existir uma logica para tratar isso
		return true;
	}

}
