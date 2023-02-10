package com.banco.backend.service;

import java.util.List;
import java.util.UUID;

import com.banco.backend.dto.CuentaDTO;

public interface CuentaService {
	
	public List<CuentaDTO> listarCuentasPorBancoId(long bancoId);
	
	public List<CuentaDTO> listarCuentasPorUsuarioId(long bancoId,long usuarioId);
	
	public List<CuentaDTO> listarCuentasPorTipo(long bancoId,long tipoCuentaId);
	
	public CuentaDTO buscarCuentaPorId(long bancoId,UUID cuentaId);
	
	public CuentaDTO crearCuenta(long bancoId,long usuarioId,long tipoCuentaId,CuentaDTO cuentaDTO);
	
	public void deshabilitarCuentaPorUsuario(long bancoId,long usuarioId,UUID cuentaId);
	
	public void deshabilitarCuentaPorAdmin(long bancoId,UUID cuentaId);
	
	public void habilitarCuentaPorUsuario(long bancoId,long usuarioId,UUID cuentaId);
	
	public void habilitarCuentaPorAdmin(long bancoId,UUID cuentaId);
	
	public void eliminarCuenta(long bancoId,UUID cuentaId);
	
	

}
