package com.banco.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "movimientos")
public class Movimiento extends AudtiModel {
	
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonBackReference(value = "movimientos-cuenta")
	@ManyToOne
	@JoinColumn(name = "cuenta_id")
	private Cuenta cuenta;

	@OneToOne
	@JoinColumn(name = "tipoTransaccion_id")
	private TipoTransaccion tipoTransaccion;

	private float Monto;

	@OneToOne
	@JoinColumn(name = "detalle_movimiento_id")
	private DetalleMovimiento detalleMovimiento;


}
