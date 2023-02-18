package com.banco.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "solicitud_habilitar_cuenta")
public class SolicitudHabilitarCuenta extends AudtiModel {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String motivo;
	private String estado;
	
	@JsonBackReference(value = "cuenta-solicitud")
	@ManyToOne
	@JoinColumn(name = "cuenta_id")
	private Cuenta cuenta;
}
