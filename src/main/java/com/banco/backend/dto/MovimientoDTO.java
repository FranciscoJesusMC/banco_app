package com.banco.backend.dto;

import java.util.Date;

import com.banco.backend.entity.DetalleMovimiento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoDTO {
	
	private Date fechaCreacion;

	private TipoTransaccionDTO tipoTransaccion;

	private float monto;

	private DetalleMovimiento detalleMovimiento;


}
