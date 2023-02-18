package com.banco.backend.service;

import java.util.List;
import java.util.UUID;

import com.banco.backend.dto.CuentaDTO;

public interface CuentaService {
	
	public List<CuentaDTO> listarCuentasPorUsuarioId(long bancoId,long usuarioId);
	
	public CuentaDTO buscarCuentaPorId(long bancoId,UUID cuentaId);
	
	public CuentaDTO crearCuenta(long bancoId,long usuarioId,long tipoCuentaId);
	
	public void deshabilitarCuentaPorUsuario(long bancoId,long usuarioId,UUID cuentaId);
	
		
}
