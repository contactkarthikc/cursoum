package com.nelioalves.cursomc.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProfilePicture implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	// IDENTITY funciona bem com H@, mas dependendo do banco tem que alterar
	// (sequence por ex.)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private String contentType;
	@Column(columnDefinition="BLOB")
	private byte[] arquivo;

	public ProfilePicture() {}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

}
