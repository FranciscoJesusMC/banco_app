package com.banco.backend.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.TipoTransaccionDTO;
import com.banco.backend.entity.TipoTransaccion;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.TipoTransaccionMapper;
import com.banco.backend.repository.TipoTransaccionRepositorio;
import com.banco.backend.service.TipoTransaccionService;

@Service
public class TipoTransaccionServiceImpl implements TipoTransaccionService {
	
	@Autowired
	private TipoTransaccionRepositorio tipoTransaccionRepositorio;
	
	@Autowired
	private TipoTransaccionMapper mapper;
	
	@Autowired
	private AccionesAdminServiceImpl acciones;

	@Override
	public List<TipoTransaccionDTO> listarTiposDeTransaccion() {
		List<TipoTransaccion> tipos = tipoTransaccionRepositorio.findAll();
		if(tipos.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "No hay tipos de transaccion registrados");
		}
		return tipos.stream().map(tipo -> mapper.tipoTransacciontoTipoTransaccionDTO(tipo)).collect(Collectors.toList());
	}

	@Override
	public TipoTransaccionDTO crearTipoDeTransaccion(TipoTransaccionDTO tipoTransaccionDTO) {
		TipoTransaccion tipoTransaccion = mapper.tipoTransaccionDTOtoTipoTransaccion(tipoTransaccionDTO);
		
		TipoTransaccion nuevoTipo = tipoTransaccionRepositorio.save(tipoTransaccion);
		
		TipoTransaccionDTO guardarTipo = mapper.tipoTransacciontoTipoTransaccionDTO(nuevoTipo);
		
		acciones.realizarAccion("Crear tipo de transaccion", tipoTransaccion.getId(),null, "Tipo de transaccion creado con exito");
		return guardarTipo;
	}

	@Override
	public void eliminarTipoDeTransaccion(long tipoTransaccionId) {
		TipoTransaccion tipoTransaccion = tipoTransaccionRepositorio.findById(tipoTransaccionId).orElseThrow(()-> new ResourceNotFoundException("TipoTransaccion", "id", tipoTransaccionId));
		tipoTransaccionRepositorio.delete(tipoTransaccion);
		
		acciones.realizarAccion("Eliminar tipo de transaccion", tipoTransaccionId,null, "Tipo de transaccion eliminado con exito");
		
		
	}

}
