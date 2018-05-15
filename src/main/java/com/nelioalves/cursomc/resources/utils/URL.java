package com.nelioalves.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {

	public static List<Integer> decodeIntList(String s){
		/*
		List<Integer> list = new ArrayList<>();
		String[] vet = s.split(",");
		for (int i = 0; i < vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		return list;*/
			   // s.split(",") - converte a string em array usando o separador ",";
			   // Arrays.asList(s.split(",")) - converte o array em list
			   // stream retorna cada elemento da lista 
		return Arrays.asList(s.split(",")).stream()
				// converte cada elemento passado para int
				.map(x -> Integer.parseInt(x))
				// transforma tudo em uma lista novamente
				.collect(Collectors.toList());
	}
	
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
