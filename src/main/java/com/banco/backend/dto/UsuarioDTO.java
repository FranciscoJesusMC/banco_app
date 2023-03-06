package com.banco.backend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
	
	private Long id;
	
	@NotEmpty(message = "EL campo nombre no puede estar vacio")
	@Size(min = 2 , max = 30,message = "El nombre debe tener un minimo de 2 caracteres y un maximo de 30")
	@Pattern(regexp = "^[a-zA-Z]+$",message = "Solo esta permitido ingresar letras")
	private String nombre;
	
	@NotEmpty(message = "EL campo apellido no puede estar vacio")
	@Size(min = 2 , max = 30,message = "El apellido debe tener un minimo de 2 caracteres y un maximo de 30")
	@Pattern(regexp = "^[a-zA-Z ]+$",message = "Solo esta permitido ingresar letras")
	private String apellido;
	
	@NotNull(message = "EL campo dni no puede estar vacio")
	@Size(min = 8, max = 8,message = "EL dni debe tener 8 digitos")
	@Pattern(regexp = "^[0-9]+$",message = "Solo esta permitido ingresar numeros")
	private String dni;
	
	@NotNull(message = "El campoo edad no puede estar vacio")
	@Min(value = 18,message = "La edad minimo es 18")
	private int edad;
	
	@NotEmpty(message = "El genero no puede estar vacio")
	@Size(min = 1, max = 1,message = "El genero debe ser M (Masculino) o F (Femenino)")
	private String genero;
	
	@NotEmpty(message = "El campo emial no puede estar vacio")
	@Email
	private String email;
	
	@NotNull(message = "El campo celular no puede estar vacio")
	@Size(min = 9 , max = 9, message = "El celular debe tener 9 digitos")
	@Pattern(regexp = "^[0-9]+$",message = "Solo esta permitido ingresar numeros")
	private String celular;
	
	@NotEmpty(message = "EL campo username no puede estar vacio")
	@Size(min = 2 , max = 30,message = "El username debe tener un minimo de 2 caracteres y un maximo de 30")
	@Pattern(regexp = "^[a-zA-Z]+$",message = "Solo esta permitido ingresar letras")
	private String username;
	
	@NotEmpty(message = "EL campo password no puede estar vacio")
	@Size(min = 2 , max = 30,message = "El password debe tener un minimo de 2 caracteres y un maximo de 30")
	@Pattern(regexp = "^[a-zA-Z0-9]+$",message = "Solo esta permitido ingresar letras")
	private String password;

}
