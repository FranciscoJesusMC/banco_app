package com.banco.backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

import com.banco.backend.dto.PrestamoDTO;
import com.banco.backend.entity.Prestamo;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {


	Prestamo prestamoDTOtoPrestamo(PrestamoDTO prestamoDTO);
	PrestamoDTO prestamotoPrestamoDTO(Prestamo prestamo);
}
