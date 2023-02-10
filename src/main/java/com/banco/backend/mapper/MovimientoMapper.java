package com.banco.backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;


import com.banco.backend.dto.MovimientoDTO;
import com.banco.backend.entity.Movimiento;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

	
	Movimiento movimientoDTOtoMovimiento(MovimientoDTO movimientoDTO);
	MovimientoDTO movimientotoMovimientoDTO(Movimiento movimiento);
}
