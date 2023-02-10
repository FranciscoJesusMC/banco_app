package com.banco.backend.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "fechaCreacion", "fechaActualizacion"}, allowGetters = true)
public abstract class AudtiModel implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion",nullable = false)
	@CreatedDate
	private Date fechaCreacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_actualizacion",nullable = false)
	@LastModifiedDate
	private Date fechaActualizacion;
	
	//Si neceistamos la hora aparte
//	@Temporal(TemporalType.TIME)
//	@Column(name = "hora_creacion")
//	@CreatedDate
//	private Date horaCreacion;
//	
//	@Temporal(TemporalType.TIME)
//	@Column(name = "hora_actualizacion")
//	@LastModifiedDate
//	private Date horaActualizacion;

}
