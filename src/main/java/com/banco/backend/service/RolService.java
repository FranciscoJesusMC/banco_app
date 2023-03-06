package com.banco.backend.service;

import java.util.List;

import com.banco.backend.dto.RolDTO;


public interface RolService {

	public List<RolDTO> listarRoles();
	
	public RolDTO crearRol(RolDTO rolDTO);
	
	public void eliminarRol(long rolId);
}
