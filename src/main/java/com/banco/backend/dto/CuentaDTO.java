package com.banco.backend.dto;


import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaDTO {

	private UUID id;
		
	private int depositosDelDia;
	
	private int retirosDelDia;
	
	private BigDecimal saldo;
	
	private BigDecimal limiteDelDia;

	private TipoCuentaDTO tipoCuenta;
}
