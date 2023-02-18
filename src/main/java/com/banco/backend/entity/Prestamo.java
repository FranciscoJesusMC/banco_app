package com.banco.backend.entity;

import javax.persistence.Column;
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
@NoArgsConstructor
@Entity
@Table(name = "prestamos")
public class Prestamo extends AudtiModel {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double importe;
	
	private int cuotas;

	@Column(name = "tasa_de_interes")
	private double tasaDeInteres;
	
	@Column(name = "interes_a_pagar")
	private double interesApagar;
	
	@Column(name = "cuota_mensual")
	private double cuotaMensual;
	
	@Column(name = "deuda_total")
	private double deudaTotal;
	
	private String estado;
	
	@JsonBackReference(value = "cuenta-prestamo")
	@ManyToOne
	@JoinColumn(name = "cuenta_id")
	private Cuenta cuenta;

}
