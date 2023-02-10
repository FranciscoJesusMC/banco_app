package com.banco.backend.service;

import java.util.List;

import com.banco.backend.dto.TipoCuentaDTO;

public interface TipoCuentaService {
	
	public List<TipoCuentaDTO> listarTiposDecuenta();

	public TipoCuentaDTO buscarTipoCuentaPorId(long tipoCuentaId);
	
	public TipoCuentaDTO crearTipoCuenta(TipoCuentaDTO tipoCuentaDTO);
	
	public TipoCuentaDTO actualizarTipoCuenta(long tipoCuentaId,TipoCuentaDTO tipoCuentaDTO);
	
	public void eliminarTipoCuenta(long tipoCuentaId);

}
