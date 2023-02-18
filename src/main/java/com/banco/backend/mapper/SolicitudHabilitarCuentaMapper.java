package com.banco.backend.mapper;

import org.mapstruct.Mapper;

import com.banco.backend.dto.SolicitudHabilitarCuentaDTO;
import com.banco.backend.entity.SolicitudHabilitarCuenta;

@Mapper(componentModel = "spring")
public interface SolicitudHabilitarCuentaMapper {

	SolicitudHabilitarCuenta solicitudHabilitarCuentaDTOtoSolicitudHabilitarCuenta(SolicitudHabilitarCuentaDTO solicitudHabilitarCuentaDTO);
	SolicitudHabilitarCuentaDTO solicitudHabilitarCuentatoSolicitudHabilitarCuentaDTO(SolicitudHabilitarCuenta solicitudHabilitarCuenta);
}
