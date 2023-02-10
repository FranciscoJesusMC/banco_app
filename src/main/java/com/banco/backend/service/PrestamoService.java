package com.banco.backend.service;

import java.util.List;
import java.util.UUID;

import com.banco.backend.dto.PrestamoDTO;

public interface PrestamoService {

	public List<PrestamoDTO> listarPrestamosPorUsuario(long bancoId,long usuarioId,UUID cuentaId);
	
	public PrestamoDTO generarPrestamo(long bancoId,long usuarioId,UUID cuentaId,PrestamoDTO prestamoDTO);
	
	public void aprobarPrestamo(long bancoId,UUID cuentaId,long prestamoId);
	
	public void rechazarPrestamo(long bancoId,UUID cuentaId,long prestamoId);
	
}
