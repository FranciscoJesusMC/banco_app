package com.banco.backend.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetalles {
	
	private Date marcaDeTiempo;
	private String mensaje;
	private String detalles;

}
