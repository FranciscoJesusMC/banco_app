package com.banco.backend.dto;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

import com.banco.backend.entity.DetalleMovimiento;
import com.banco.backend.entity.TipoTransaccion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferenciaDTO {
	
	private long id;

	private Date fechaCreacion;

	private Time horaCreacion;

	private TipoTransaccion tipoTransaccion;

	private UUID cuentaDestino;
	
	private float monto;
	
	private DetalleMovimiento detalleMovimiento;

}
