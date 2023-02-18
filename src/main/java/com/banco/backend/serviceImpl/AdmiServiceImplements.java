package com.banco.backend.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.dto.PrestamoDTO;
import com.banco.backend.dto.SolicitudHabilitarCuentaDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Cuenta;
import com.banco.backend.entity.Prestamo;
import com.banco.backend.entity.SolicitudHabilitarCuenta;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.CuentaMapper;
import com.banco.backend.mapper.PrestamoMapper;
import com.banco.backend.mapper.SolicitudHabilitarCuentaMapper;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.CuentaRepositorio;
import com.banco.backend.repository.PrestamoRepositorio;
import com.banco.backend.repository.SolicitudHabilitarCuentaRepositorio;
import com.banco.backend.service.AdminService;

@Service
public class AdmiServiceImplements implements AdminService {
		
	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	
	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private PrestamoRepositorio prestamoRepositorio;
	
	@Autowired
	private SolicitudHabilitarCuentaRepositorio solicitudRepositorio;
	
	@Autowired
	private PrestamoMapper mapperPrestamo;
	
	@Autowired
	private CuentaMapper mapperCuenta;
	
	@Autowired
	private SolicitudHabilitarCuentaMapper mapperSolicitud;


	@Override
	public void actualizarLimiteDiaroDeSaldo() {
		List<Cuenta> cuentas = cuentaRepositorio.findAll();
		for(Cuenta cuenta : cuentas) {
			cuenta.setLimiteDelDia(new BigDecimal(500.00));
			cuentaRepositorio.save(cuenta);
		}
		
	}

	@Override
	public void inhabilitarTodasLasCuenta() {
		List<Cuenta> cuentas = cuentaRepositorio.findAll();
		for(Cuenta cuenta : cuentas) {
			cuenta.setEstado("Deshabilitada");
			cuentaRepositorio.save(cuenta);
		}
		
	}

	@Override
	public void habilitarTodasLasCuentas() {
		List<Cuenta> cuentas = cuentaRepositorio.findAll();
		for(Cuenta cuenta : cuentas) {
			cuenta.setEstado("Habilitada");
			cuentaRepositorio.save(cuenta);
		}
		
	}
	
	@Override
	public void aprobarPrestamo(long bancoId, UUID cuentaId,long prestamoId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Prestamo prestamo = prestamoRepositorio.findById(prestamoId).orElseThrow(()-> new ResourceNotFoundException("Prestamo", "id", prestamoId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		if(cuenta.getEstado().equals("Deshabilitada")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta Deshabilitada");
		}
		
		if(!prestamo.getCuenta().getId().equals(cuenta.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La prestamo : " + prestamoId + "  no pertenece a la cuenta : +" + cuentaId);
		}
		

		BigDecimal saldoPrestamo = new BigDecimal(prestamo.getImporte());
		
		BigDecimal saldoCuenta = new BigDecimal(cuenta.getSaldo().doubleValue());
		
		BigDecimal nuevoSaldo =  saldoCuenta.add(saldoPrestamo);

		
		prestamo.setEstado("Aprobado");
		prestamoRepositorio.save(prestamo);
		
		
		cuenta.setSaldo(nuevoSaldo);
		cuentaRepositorio.save(cuenta);
		
	}

	@Override
	public void rechazarPrestamo(long bancoId, UUID cuentaId, long prestamoId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Prestamo prestamo = prestamoRepositorio.findById(prestamoId).orElseThrow(()-> new ResourceNotFoundException("Prestamo", "id", prestamoId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		if(cuenta.getEstado().equals("Deshabilitada")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta Deshabilitada");
		}
		
		if(!prestamo.getCuenta().getId().equals(cuenta.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La prestamo : " + prestamoId + "  no pertenece a la cuenta : +" + cuentaId);
		}
		
		prestamo.setEstado("Rechazado");
		prestamoRepositorio.save(prestamo);
		

		
		
		
	}
	
	@Override
	public void habilitarCuentaPorAdmin(long bancoId, UUID cuentaId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta con el id : "+ cuentaId + "no pertenece al banco con el id: " +bancoId );
		}
		
		if(cuenta.getEstado().equals("Habilitada")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta actualmente esta habilitado");
		}
		
		cuenta.setEstado("Habilitada");
		cuentaRepositorio.save(cuenta);
		
	}
	
	@Override
	public void deshabilitarCuentaPorAdmin(long bancoId, UUID cuentaId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta con el id : "+ cuentaId + "no pertenece al banco con el id: " +bancoId );
		}
		
		if(cuenta.getEstado().equals("Deshabilitada")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta actualmente esta inhabilitado");
		}
		
		cuenta.setEstado("Deshabilitada");
		cuentaRepositorio.save(cuenta);
		
	}

	@Override
	public List<PrestamoDTO> listarPrestamos() {
		List<Prestamo> prestamos = prestamoRepositorio.findAllByOrderByFechaCreacionDesc();
		if(prestamos.isEmpty()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "No hay prestamos solicitados");
			
		}
		return prestamos.stream().map(prestamo ->mapperPrestamo.prestamotoPrestamoDTO(prestamo)).collect(Collectors.toList());
	}

	@Override
	public List<CuentaDTO> listarCuentasPorBancoId(long bancoId) {
		bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<Cuenta> cuentas = cuentaRepositorio.findByBancoId(bancoId);
		
		if(cuentas.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "No hay cuentas registradas en el banco con el id: " +bancoId);
		}
		return cuentas.stream().map(cuenta -> mapperCuenta.cuentatoCuentaDTO(cuenta)).collect(Collectors.toList());
	}

	@Override
	public List<CuentaDTO> listarCuentasPorTipo(long bancoId, long tipoCuentaId) {
		bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<Cuenta> cuentas = cuentaRepositorio.findByTipoCuentaId(tipoCuentaId);
		if(cuentas.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "No hay cuentas con el tipo de cuenta id :"+ tipoCuentaId + " en el banco con el id :" + bancoId);
		}
		
		return cuentas.stream().map(cuenta -> mapperCuenta.cuentatoCuentaDTO(cuenta)).collect(Collectors.toList());
		
	}
	
	@Override
	public void eliminarCuenta(long bancoId, UUID cuentaId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta con el id : "+ cuentaId + "no pertenece al banco con el id: " +bancoId );
		}
		
		cuentaRepositorio.delete(cuenta);
		
	}

	@Override
	public List<SolicitudHabilitarCuentaDTO> listarSolicitudesPendientes(long bancoId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<SolicitudHabilitarCuenta> solicitudes = solicitudRepositorio.findAll();
		
		List<SolicitudHabilitarCuenta> segunBanco = new ArrayList<>();
		
		solicitudes.forEach(solicitud ->{
			if(solicitud.getCuenta().getBanco().getId().equals(banco.getId()) && solicitud.getEstado().equals("Pendiente") ) {
				segunBanco.add(solicitud);
			}
		});
		
		if(segunBanco.isEmpty()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "No hay solicitudes para habilitar cuentas en el banco :" + bancoId);
		}
		
		return segunBanco.stream().map(porBanco -> mapperSolicitud.solicitudHabilitarCuentatoSolicitudHabilitarCuentaDTO(porBanco)).collect(Collectors.toList());
	
	}

	@Override
	public List<SolicitudHabilitarCuentaDTO> listarSolicitudesAprobadas(long bancoId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<SolicitudHabilitarCuenta> solicitudes = solicitudRepositorio.findAll();
		
		List<SolicitudHabilitarCuenta> segunBanco = new ArrayList<>();
		
		solicitudes.forEach(solicitud ->{
			if(solicitud.getCuenta().getBanco().getId().equals(banco.getId()) && solicitud.getEstado().equals("Aprobado") ) {
				segunBanco.add(solicitud);
			}
		});
		
		if(segunBanco.isEmpty()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "No hay solicitudes aprobadas en el banco :" + bancoId);
		}
		
		return segunBanco.stream().map(porBanco -> mapperSolicitud.solicitudHabilitarCuentatoSolicitudHabilitarCuentaDTO(porBanco)).collect(Collectors.toList());
	
	}

	@Override
	public List<SolicitudHabilitarCuentaDTO> listarSolicitudesRechazadas(long bancoId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<SolicitudHabilitarCuenta> solicitudes = solicitudRepositorio.findAll();
		
		List<SolicitudHabilitarCuenta> segunBanco = new ArrayList<>();
		
		solicitudes.forEach(solicitud ->{
			if(solicitud.getCuenta().getBanco().getId().equals(banco.getId()) && solicitud.getEstado().equals("Rechazado") ) {
				segunBanco.add(solicitud);
			}
		});
		
		if(segunBanco.isEmpty()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "No hay solicitudes rechazadas cuentas en el banco :" + bancoId);
		}
		
		return segunBanco.stream().map(porBanco -> mapperSolicitud.solicitudHabilitarCuentatoSolicitudHabilitarCuentaDTO(porBanco)).collect(Collectors.toList());
	
	}

	@Override
	public SolicitudHabilitarCuentaDTO buscarSolicitud(long bancoId, long solicitudId) {
	
		bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<SolicitudHabilitarCuenta> listaSolicitudes = solicitudRepositorio.findAll();
		
		for(SolicitudHabilitarCuenta solicitud : listaSolicitudes) {
			if(solicitud.getId().equals(solicitudId) && solicitud.getCuenta().getBanco().getId().equals(bancoId)) {
				return mapperSolicitud.solicitudHabilitarCuentatoSolicitudHabilitarCuentaDTO(solicitud);
			}else {
				throw new BancoAppException(HttpStatus.BAD_REQUEST, "No se encontro la solicitud con el id: " + solicitudId);
			}
		}
		
		return null;
		
	
	}

	@Override
	public List<SolicitudHabilitarCuentaDTO> listarTodasLasSolicitudes(long bancoId) {
		
		bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<SolicitudHabilitarCuenta> solicitudes = solicitudRepositorio.findAll();
		
		List<SolicitudHabilitarCuenta> solicitudesPorBanco = new ArrayList<>();
		
		for(SolicitudHabilitarCuenta solicitud : solicitudes) {
			if(solicitud.getCuenta().getBanco().getId().equals(bancoId)) {
				solicitudesPorBanco.add(solicitud);
			}
		}
		
		if(solicitudesPorBanco.isEmpty()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "No hay solicitudes para habilitar cuenta en el banco : " +bancoId);
		}
		
		
		return solicitudesPorBanco.stream().map(solicitud -> mapperSolicitud.solicitudHabilitarCuentatoSolicitudHabilitarCuentaDTO(solicitud)).collect(Collectors.toList());
	}

}
