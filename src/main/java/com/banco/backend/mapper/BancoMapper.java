package com.banco.backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

import com.banco.backend.dto.BancoDTO;
import com.banco.backend.entity.Banco;

@Mapper(componentModel = "spring")
public interface BancoMapper {

	Banco bancoDTOtoBanco(BancoDTO bancoDTO);
	BancoDTO bancotoBancoDTO(Banco banco);
}
