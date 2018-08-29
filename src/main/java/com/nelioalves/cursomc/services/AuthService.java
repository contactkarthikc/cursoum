package com.nelioalves.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random();

	// gera uma nova senha, grava a nova senha no banco e envia para o usuário
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email nao encontrado");
		}

		// gera nova senha aleatoria
		String newPass = newPassord();
		// grava nova senha
		cliente.setSenha(pe.encode(newPass));
		clienteRepository.save(cliente);
		
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassord() {
		char[] vet = new char[10];
		for (int i = 0; i < vet.length; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = random.nextInt(3);
		if(opt ==0) { //gera um digito
			//gero um numero aleatorio de 0-9 (por isso random(10)) e somo ao 48 que é o codigo unicode que corresponde ao 0 (zero)
			return (char)(random.nextInt(10)+48);
		}else if (opt == 1) { //gera letra maiuscula
			//gero um numero aleatorio de 26 letras MAIUSCULAS possiveis (por isso random(26)) e somo ao 65 que é o codigo unicode que corresponde ao A
			return (char)(random.nextInt(26)+65);
		}else { //gera letra minuscula
			return (char)(random.nextInt(26)+97);
		}
	}
}
