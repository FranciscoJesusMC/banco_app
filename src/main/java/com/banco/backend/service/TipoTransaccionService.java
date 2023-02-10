package com.banco.backend.service;

import java.util.List;

import com.banco.backend.dto.TipoTransaccionDTO;

public interface TipoTransaccionService {
	
	public List<TipoTransaccionDTO> listarTiposDeTransaccion();
	
	public TipoTransaccionDTO crearTipoDeTransaccion(TipoTransaccionDTO tipoTransaccionDTO);
	
	public void eliminarTipoDeTransaccion(long tipoTransaccionId);

}
