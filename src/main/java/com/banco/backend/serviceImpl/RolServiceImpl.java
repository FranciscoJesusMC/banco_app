package com.banco.backend.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.RolDTO;
import com.banco.backend.entity.Rol;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.mapper.RolMapper;
import com.banco.backend.repository.RolRepositorio;
import com.banco.backend.service.RolService;

@Service
public class RolServiceImpl implements RolService {

	@Autowired
	private RolRepositorio rolRepositorio;
	
	@Autowired
	private RolMapper rolMapper;

	@Override
	public List<RolDTO> listarRoles() {
		List<Rol> roles = rolRepositorio.findAll();
		if(roles.isEmpty()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST,"No hay roles registrados");
		}
		return roles.stream().map(rol ->rolMapper.RoltoRolDTO(rol)).collect(Collectors.toList());
	}

	@Override
	public RolDTO crearRol(RolDTO rolDTO) {
		Rol rol = rolMapper.RolDTOtoRol(rolDTO);
		
		Rol nuevoRol = rolRepositorio.save(rol);
		
		RolDTO guardarRol = rolMapper.RoltoRolDTO(nuevoRol);
		
		return guardarRol;
	}

	@Override
	public void eliminarRol(long rolId) {
		// TODO Auto-generated method stub
		
	}
}
