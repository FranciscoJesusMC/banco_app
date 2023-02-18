package com.banco.backend.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginacionMovimiento {

	public List<MovimientoDTO> contenido;
	public int numeroDePagina;
	public int medidaDePagina;
	public long totalElementos;
	public int totalPaginas;
	public boolean ultima;
}
