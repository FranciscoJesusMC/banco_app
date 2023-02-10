package com.banco.backend.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaDTO {

	private UUID id;
		
	private int depositosDelDia;
	
	private int retirosDelDia;

	private TipoCuentaDTO tipoCuenta;
}
