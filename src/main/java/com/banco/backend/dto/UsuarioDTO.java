package com.banco.backend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
	

	@NotEmpty(message = "EL campo nombre no puede estar vacio")
	@Size(min = 2 , max = 30,message = "El nombre debe tener un minimo de 2 caracteres y un maximo de 30")
	private String nombre;
	
	@NotEmpty(message = "EL campo apellido no puede estar vacio")
	@Size(min = 2 , max = 30,message = "El apellido debe tener un minimo de 2 caracteres y un maximo de 30")
	private String apellido;
	
	@NotEmpty(message = "EL campo dni no puede estar vacio")
	@Size(max = 8,message = "El campo dni acepta maximo 8 digitos")
	private int dni;
	
	@NotEmpty(message = "El campoo edad no puede estar vacio")
	@Min(value = 18,message = "La edad minimo debe ser 18")
	private int edad;
	
	@NotEmpty(message = "El campo emial no puede estar vacio")
	@Email
	private String email;
	
	@NotEmpty(message = "El campo celular no puede estar vacio")
	@Size(max = 9,message = "El campo celular debe tener 9 digitos")
	private int celular;

}
