package com.banco.backend.jwt;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtRequest {

	@NotBlank(message = "El campo usernameOrEmail no puede estar vacio")
	private String usernameOrEmail;
	
	@NotBlank(message = "El campo password no puede estar vacio")
	private String password;
}
