package com.banco.backend.dto;

import java.math.BigDecimal;
import java.sql.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrestamoDTO {

	private long id;
	
	@NotEmpty(message = "EL campo importe no puede estar vacio")
	@Max(1000000)
	private float importe;
	
	@NotEmpty(message = "EL campo cuotas no puede estar vacio")
	@Max(value = 24,message = "El maxico de cuotas es 24")
	private int cuotas;

	private BigDecimal tasaDeInteres;
	
	private BigDecimal cuotaMensual;
	
	private BigDecimal deudaTotal;

	private String estado;
	
	private Date fechaCreacion;
}
