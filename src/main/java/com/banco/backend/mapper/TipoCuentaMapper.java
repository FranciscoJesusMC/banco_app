package com.banco.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.banco.backend.dto.TipoCuentaDTO;
import com.banco.backend.entity.TipoCuenta;

@Mapper(componentModel = "spring")
public interface TipoCuentaMapper {


	TipoCuenta tipoCuentaDTOtoTipoCuenta(TipoCuentaDTO tipoCuentaDTO);
	TipoCuentaDTO tipoCuentatoTipoCuentaDTO(TipoCuenta tipoCuenta);
}
