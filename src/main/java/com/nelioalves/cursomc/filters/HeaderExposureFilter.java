package com.nelioalves.cursomc.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

// um filtro captura e processa todas as requisições
@Component
public class HeaderExposureFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	
	// para que o angular consiga acessar o location é preciso expor essa variavel no header
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse)response;
		res.addHeader("access-control-expose-headers", "location");
		// encaminha a requisicao para seu ciclo normal
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}

}
