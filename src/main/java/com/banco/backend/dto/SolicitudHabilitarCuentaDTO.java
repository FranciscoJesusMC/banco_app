package com.banco.backend.dto;



import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudHabilitarCuentaDTO {
	
	@NotEmpty
	@NotNull(message = "El campo motivo no puede estar vacio")
	@Size(min = 5,max = 100,message = "El motivo debe tener un minimo de 5 caracteres y un maximo de 100")
	@Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Solo esta permitido ingresar letras y nuemros")
	private String motivo;
	
	private String estado;
	private CuentaDTO cuenta;

}
