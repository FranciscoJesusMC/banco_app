package com.banco.backend.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BancoDTO {


	@NotEmpty(message = "EL campo nombre no puede estar vacio")
	@Size(min = 5,message = "Nombre del banco debe contener al menos 5 caracteres")
	private String nombre;
	
}
