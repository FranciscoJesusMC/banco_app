package com.banco.backend.service;

import java.util.List;
import java.util.UUID;

import com.banco.backend.dto.SolicitudHabilitarCuentaDTO;

public interface SolicitudHabilitarCuentaService {
	
	public List<SolicitudHabilitarCuentaDTO> listarSolicitudes(long bancoId,long usuarioId,UUID cuentaId);

	public SolicitudHabilitarCuentaDTO crearSolicitudHabilitarCuenta(long bancoId,long usuarioId,UUID cuentaId,SolicitudHabilitarCuentaDTO solicitudHabilitarCuentaDTO);

}
