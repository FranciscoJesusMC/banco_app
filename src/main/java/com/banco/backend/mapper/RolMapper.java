package com.banco.backend.mapper;

import org.mapstruct.Mapper;

import com.banco.backend.dto.RolDTO;
import com.banco.backend.entity.Rol;

@Mapper(componentModel = "spring")
public interface RolMapper {
	
	Rol RolDTOtoRol(RolDTO rolDTO);
	RolDTO RoltoRolDTO(Rol rol);

}
