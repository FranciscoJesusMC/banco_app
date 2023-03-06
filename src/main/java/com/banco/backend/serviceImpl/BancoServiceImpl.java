package com.banco.backend.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.BancoDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.BancoMapper;
import com.banco.backend.mapper.BancoMapperImpl;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.service.BancoService;

@Service
public class BancoServiceImpl  implements BancoService {

	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private BancoMapper mapper = new BancoMapperImpl();
	
	@Autowired
	private AccionesAdminServiceImpl acciones;
	
	
	@Override
	public List<BancoDTO> listarBancos() {
		List<Banco> bancos = bancoRepositorio.findAll();
		
		if(bancos.isEmpty()) {
			throw new BancoAppException( HttpStatus.NOT_FOUND,"No hay bancos registrados");
		}
		
		return bancos.stream().map(banco -> mapper.bancotoBancoDTO(banco)).collect(Collectors.toList());
	}
	

	@Override
	public BancoDTO buscarBancoPorId(long bancoId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		return mapper.bancotoBancoDTO(banco);
	}

	@Override
	public BancoDTO crearBanco(BancoDTO bancoDTO) {
		Banco banco = mapper.bancoDTOtoBanco(bancoDTO);
		
		Banco nuevoBanco = bancoRepositorio.save(banco);
		
		BancoDTO guardarBanco = mapper.bancotoBancoDTO(nuevoBanco);
		
		return guardarBanco;
	}

	@Override
	public BancoDTO actualizarBanco(long bancoId, BancoDTO bancoDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		banco.setNombre(bancoDTO.getNombre());
		
		Banco actualizarBanco = bancoRepositorio.save(banco);
		
		BancoDTO guardarBanco = mapper.bancotoBancoDTO(actualizarBanco);
		
		acciones.realizarAccion("Actualizar banco", bancoId,null, "Banco actualizado con exito");
		return guardarBanco;
		
	}

	@Override
	public void eliminarBanco(long bancoId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		bancoRepositorio.delete(banco);
		
		acciones.realizarAccion("Eliminar banco", bancoId,null, "Banco eliminado con exito");
		
	}
	
}
