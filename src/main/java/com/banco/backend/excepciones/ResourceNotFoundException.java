package com.banco.backend.excepciones;

import java.util.UUID;

import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus
public class ResourceNotFoundException  extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String nombreDelRecurso;
	private String nombreDelCampo;
	private long valorDelCampo;
	private UUID valorDelCampo2;

	public ResourceNotFoundException(String nombreDelRecurso, String nombreDelCampo, long valorDelCampo) {
		super(String.format("%s no encontrado para el %s : '%s'",nombreDelRecurso,nombreDelCampo,valorDelCampo));
		this.nombreDelRecurso = nombreDelRecurso;
		this.nombreDelCampo = nombreDelCampo;
		this.valorDelCampo = valorDelCampo;
	}

	public ResourceNotFoundException(String nombreDelRecurso, String nombreDelCampo, UUID valorDelCampo2) {
		super(String.format("%s no encontrado para el %s : '%s'",nombreDelRecurso,nombreDelCampo,valorDelCampo2));
		this.nombreDelRecurso = nombreDelRecurso;
		this.nombreDelCampo = nombreDelCampo;
		this.valorDelCampo2 = valorDelCampo2;
	}

}
