package com.banco.backend.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoCuentaDTO {
	
	private Long id;

	@NotEmpty(message = "El campo nombre no puede estar vacio")
	private String nombre;
	@NotEmpty(message = "El campo descripcion no puede estar vacio")
	private String descripcion;
}
