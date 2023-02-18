package com.banco.backend.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.SolicitudHabilitarCuentaDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Cuenta;
import com.banco.backend.entity.SolicitudHabilitarCuenta;
import com.banco.backend.entity.Usuario;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.SolicitudHabilitarCuentaMapper;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.CuentaRepositorio;
import com.banco.backend.repository.SolicitudHabilitarCuentaRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.service.SolicitudHabilitarCuentaService;

@Service
public class SolicitudHabilitarCuentaServiceImpl implements SolicitudHabilitarCuentaService {
	
	@Autowired
	private SolicitudHabilitarCuentaRepositorio repositorio;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	
	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private SolicitudHabilitarCuentaMapper mapper;
	
	
	@Override
	public List<SolicitudHabilitarCuentaDTO> listarSolicitudes(long bancoId,long usuarioId,UUID cuentaId) {
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<SolicitudHabilitarCuenta> solicitudes = repositorio.findByCuentaId(cuentaId);
		
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST,"La cuenta no pertenece al banco");
		}
		
		if(!cuenta.getUsuario().getId().equals(usuario.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al usuario");
		}
		
		
		List<SolicitudHabilitarCuenta> pendientes = new ArrayList<>();
		solicitudes.forEach(solicitud ->{
			if(solicitud.getEstado().equals("Pendiente")) {
				pendientes.add(solicitud);
			}
		});
		
		if(pendientes.isEmpty()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no tiene solicitudes de habilitacion pendientes");
		}
		
		
		return pendientes.stream().map(pendiente -> mapper.solicitudHabilitarCuentatoSolicitudHabilitarCuentaDTO(pendiente)).collect(Collectors.toList());
	}
	
	
	@Override
	public SolicitudHabilitarCuentaDTO crearSolicitudHabilitarCuenta(long bancoId,long usuarioId, UUID cuentaId,
			SolicitudHabilitarCuentaDTO solicitudHabilitarCuentaDTO) {
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<SolicitudHabilitarCuenta> solicitudes = repositorio.findByCuentaId(cuentaId);
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST,"La cuenta no pertenece al banco");
		}
		
		if(!cuenta.getUsuario().getId().equals(usuario.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al usuario");
		}
		
		if(cuenta.getEstado().equals("Habilitada")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta ya se encuentra habilitada");
		}
		
		List<SolicitudHabilitarCuenta> listaPendientes = new ArrayList<>();
		solicitudes.forEach(solicitud ->{
			if(solicitud.getEstado().equals("Pendiente")) {
				listaPendientes.add(solicitud);
			}
		});
		
		if(listaPendientes.size() > 1) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "Usted ya solicito la habilitacion de su cuenta , por favor espere una respuesta");
		}
		
		
		SolicitudHabilitarCuenta nuevaSolicitud = mapper.solicitudHabilitarCuentaDTOtoSolicitudHabilitarCuenta(solicitudHabilitarCuentaDTO);
		
		nuevaSolicitud.setEstado("Pendiente");
		nuevaSolicitud.setCuenta(cuenta);

		SolicitudHabilitarCuenta registrarSolicitud = repositorio.save(nuevaSolicitud);
		
		SolicitudHabilitarCuentaDTO guardarSolicitud = mapper.solicitudHabilitarCuentatoSolicitudHabilitarCuentaDTO(registrarSolicitud);
		
		return guardarSolicitud;
	}




}
