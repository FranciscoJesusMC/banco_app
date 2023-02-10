package com.banco.backend.excepciones;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BancoAppException extends RuntimeException  {
	
	private static final long serialVersionUID = 1L;
	private HttpStatus estado;
	private String mensaje;

	
	
}
