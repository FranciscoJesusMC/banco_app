package com.banco.backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.entity.Cuenta;

@Mapper(componentModel = "spring")
public interface CuentaMapper {

	
	Cuenta cuentaDTOtoCuenta(CuentaDTO cuentaDTO);
	CuentaDTO cuentatoCuentaDTO(Cuenta cuenta);

	

}
