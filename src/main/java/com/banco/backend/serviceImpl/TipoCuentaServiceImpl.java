package com.banco.backend.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.TipoCuentaDTO;
import com.banco.backend.entity.TipoCuenta;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.TipoCuentaMapper;
import com.banco.backend.repository.TipoCuentaRepositorio;
import com.banco.backend.service.TipoCuentaService;

@Service
public class TipoCuentaServiceImpl implements TipoCuentaService {

	
	@Autowired
	private TipoCuentaRepositorio tipoCuentaRepositorio;
	

	@Autowired
	private TipoCuentaMapper mapper;


	@Override
	public List<TipoCuentaDTO> listarTiposDecuenta() {
		List<TipoCuenta> tipos = tipoCuentaRepositorio.findAll();
		
		if(tipos.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "No hay tipos de cuenta registrados");
		}
		
		return tipos.stream().map(tipo ->mapper.tipoCuentatoTipoCuentaDTO(tipo)).collect(Collectors.toList());
	}


	@Override
	public TipoCuentaDTO buscarTipoCuentaPorId(long tipoCuentaId) {
		TipoCuenta tipoCuenta = tipoCuentaRepositorio.findById(tipoCuentaId).orElseThrow(()-> new ResourceNotFoundException("TipoCuenta", "id", tipoCuentaId));
		return mapper.tipoCuentatoTipoCuentaDTO(tipoCuenta);
	}


	@Override
	public TipoCuentaDTO crearTipoCuenta(TipoCuentaDTO tipoCuentaDTO) {
		TipoCuenta tipoCuenta =  mapper.tipoCuentaDTOtoTipoCuenta(tipoCuentaDTO);
			
		TipoCuenta nuevo = tipoCuentaRepositorio.save(tipoCuenta);
		
		TipoCuentaDTO guardarCuenta = mapper.tipoCuentatoTipoCuentaDTO(nuevo);
		
		return guardarCuenta;
	}


	@Override
	public TipoCuentaDTO actualizarTipoCuenta(long tipoCuentaId, TipoCuentaDTO tipoCuentaDTO) {
		TipoCuenta tipoCuenta = tipoCuentaRepositorio.findById(tipoCuentaId).orElseThrow(()-> new ResourceNotFoundException("TipoCuenta", "id", tipoCuentaId));
		
		tipoCuenta.setNombre(tipoCuentaDTO.getNombre());
		tipoCuenta.setDescripcion(tipoCuentaDTO.getDescripcion());
		
		TipoCuenta actualizarTipoCuenta = tipoCuentaRepositorio.save(tipoCuenta);
		
		TipoCuentaDTO guardar = mapper.tipoCuentatoTipoCuentaDTO(actualizarTipoCuenta);
		
		return guardar;
	}


	@Override
	public void eliminarTipoCuenta(long tipoCuentaId) {
		TipoCuenta tipoCuenta = tipoCuentaRepositorio.findById(tipoCuentaId).orElseThrow(()-> new ResourceNotFoundException("TipoCuenta", "id", tipoCuentaId));
		tipoCuentaRepositorio.delete(tipoCuenta);
		
	}
}
