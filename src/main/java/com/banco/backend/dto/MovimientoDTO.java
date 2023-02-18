package com.banco.backend.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.banco.backend.entity.DetalleMovimiento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoDTO {
	
	private Date fechaCreacion;

	private TipoTransaccionDTO tipoTransaccion;
	
	@NotNull
	private BigDecimal monto;

	private DetalleMovimiento detalleMovimiento;


}
