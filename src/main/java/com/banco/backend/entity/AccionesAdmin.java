package com.banco.backend.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "acciones_admin")
public class AccionesAdmin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String tipo;
	private Long entidadId;
	private UUID cuentaId;
	private String descripcion;
	private Date fechaCreacion;
	
	@ManyToOne
	private Usuario usuario;

	public AccionesAdmin( Usuario usuario,String tipo, Long entidadId, UUID cuentaId,String descripcion, Date fechaCreacion) {
		super();
		this.usuario = usuario;
		this.tipo = tipo;
		this.entidadId = entidadId;
		this.cuentaId = cuentaId;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
	}
	
	
	
	
	
}
