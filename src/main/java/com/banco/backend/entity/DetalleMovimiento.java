package com.banco.backend.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "detalle_movimiento")
public class DetalleMovimiento {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titular;

	@Column(name = "cuenta_origen", updatable = false, columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID cuentaOrigen;

	private String bancoOrigen;

	private String titularDestino;

	@Column(name = "cuenta_destino", updatable = false, columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID cuentaDestino;

	private String bancoDestino;

	private float comision;

	private float montoTotal;
}
