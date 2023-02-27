package com.banco.backend.service;

import java.util.List;

import com.banco.backend.dto.UsuarioDTO;

public interface UsuarioService {
	
	public List<UsuarioDTO> listarUsuariosPorBanco();
	
	public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO);
	
	public UsuarioDTO buscarUsuario(long usuarioId);
	
	public UsuarioDTO actualizarUsuario(long usuarioId,UsuarioDTO usuarioDTO);
	
	public void eliminarUsuario(long usuarioId);

}
