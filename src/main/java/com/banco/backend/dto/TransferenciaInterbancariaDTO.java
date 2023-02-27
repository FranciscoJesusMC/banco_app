package com.banco.backend.dto;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;


import com.banco.backend.entity.DetalleMovimiento;
import com.banco.backend.entity.TipoTransaccion;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class TransferenciaInterbancariaDTO {
	
	private long id;

	private Date fechaCreacion;

	private Time horaCreacion;

	private TipoTransaccion tipoTransaccion;

	private long cuentaDestino;

	private BigDecimal monto;
	
	private DetalleMovimiento detalleMovimiento;

}
