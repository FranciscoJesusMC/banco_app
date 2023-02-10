package com.banco.backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

import com.banco.backend.dto.TipoTransaccionDTO;
import com.banco.backend.entity.TipoTransaccion;

@Mapper(componentModel = "spring")
public interface TipoTransaccionMapper {


	TipoTransaccion tipoTransaccionDTOtoTipoTransaccion(TipoTransaccionDTO tipoTransaccionDTO);
	TipoTransaccionDTO tipoTransacciontoTipoTransaccionDTO(TipoTransaccion tipoTransaccion);
}
