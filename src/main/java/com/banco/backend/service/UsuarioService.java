package com.banco.backend.service;

import java.util.List;

import com.banco.backend.dto.UsuarioDTO;

public interface UsuarioService {
	
	public List<UsuarioDTO> listarUsuariosPorBanco(long bancoId);
	
	public UsuarioDTO crearUsuario(long bancoId,UsuarioDTO usuarioDTO);
	
	public UsuarioDTO buscarUsuario(long usuarioId);
	
	public UsuarioDTO buscarUsuarioPorBancoId(long bancoId,long usuarioId);
	
	public UsuarioDTO actualizarUsuario(long bancoId,long usuarioId,UsuarioDTO usuarioDTO);
	
	public void eliminarUsuario(long bancoId,long usuarioId);

}
