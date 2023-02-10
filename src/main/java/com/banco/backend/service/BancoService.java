package com.banco.backend.service;

import java.util.List;

import com.banco.backend.dto.BancoDTO;

public interface BancoService {
	
	public List<BancoDTO> listarBancos();
	
	public BancoDTO buscarBancoPorId(long bancoId);

	public BancoDTO crearBanco(BancoDTO bancoDTO);
	
	public BancoDTO actualizarBanco(long bancoId,BancoDTO bancoDTO);
	
	public void eliminarBanco(long bancoId);

}
