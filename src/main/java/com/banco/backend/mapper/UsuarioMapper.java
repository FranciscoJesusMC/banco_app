package com.banco.backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

import com.banco.backend.dto.UsuarioDTO;
import com.banco.backend.entity.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {


	Usuario usuarioDTOtoUsuario(UsuarioDTO usuarioDTO);
	UsuarioDTO usuariotoUsuarioDTO(Usuario usuario);
}
