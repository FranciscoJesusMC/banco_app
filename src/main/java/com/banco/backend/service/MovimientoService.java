package com.banco.backend.service;

import java.util.List;
import java.util.UUID;

import com.banco.backend.dto.MovimientoDTO;
import com.banco.backend.dto.PaginacionMovimiento;
import com.banco.backend.dto.TransferenciaDTO;

public interface MovimientoService {
	
	public PaginacionMovimiento paginarMovimientos(int numeroDepagina,int medidaDePagina,String ordenarPor,String sortDir);
	
	public List<MovimientoDTO> listarMovimientosPorCuenta(long bancoId,UUID cuentaId);
	
	public MovimientoDTO agregarSaldo(long bancoId,UUID cuentaId,MovimientoDTO movimientosDTO);
	
	public MovimientoDTO retiro(long bancoId,long usuarioId,UUID cuentaId,MovimientoDTO movimientosDTO);
	
	public MovimientoDTO transferenciaBancaria(long bancoId,long usuarioId,UUID cuentaId, TransferenciaDTO transferenciaDTO);
	
	public MovimientoDTO transferenciaInterbancaria(long bancoId,long usuarioId,UUID cuentaId,TransferenciaDTO transferenciaDTO);

	public List<MovimientoDTO> litarMovimientosRecientes(long bancoId, UUID cuentaId);
}
