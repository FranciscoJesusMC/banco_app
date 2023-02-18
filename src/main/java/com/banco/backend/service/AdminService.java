package com.banco.backend.service;

import java.util.List;
import java.util.UUID;

import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.dto.PrestamoDTO;
import com.banco.backend.dto.SolicitudHabilitarCuentaDTO;

public interface AdminService {

	public void actualizarLimiteDiaroDeSaldo();
	
	public void inhabilitarTodasLasCuenta();
	
	public void habilitarTodasLasCuentas();
	
	public void aprobarPrestamo(long bancoId,UUID cuentaId,long prestamoId);
	
	public void rechazarPrestamo(long bancoId,UUID cuentaId,long prestamoId);
	
	public void habilitarCuentaPorAdmin(long bancoId,UUID cuentaId);
	
	public void deshabilitarCuentaPorAdmin(long bancoId,UUID cuentaId);
	
	//Listas
	
	public List<PrestamoDTO> listarPrestamos();
	
	public List<CuentaDTO> listarCuentasPorTipo(long bancoId,long tipoCuentaId);
	
	public List<CuentaDTO> listarCuentasPorBancoId(long bancoId);
	
	public List<SolicitudHabilitarCuentaDTO> listarTodasLasSolicitudes(long bancoId);
	
	public List<SolicitudHabilitarCuentaDTO> listarSolicitudesPendientes(long bancoId);
	
	public List<SolicitudHabilitarCuentaDTO> listarSolicitudesAprobadas(long bancoId);
	
	public List<SolicitudHabilitarCuentaDTO> listarSolicitudesRechazadas(long bancoId);
	
	public SolicitudHabilitarCuentaDTO buscarSolicitud(long bancoId,long solicitudId);
	
	//Eliminar
	
	public void eliminarCuenta(long bancoId,UUID cuentaId);
	

}
