package com.banco.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.dto.PrestamoDTO;
import com.banco.backend.dto.SolicitudHabilitarCuentaDTO;
import com.banco.backend.service.AdminService;

@RestController
@RequestMapping("/api/accionesAdmin")
public class AdmiController {

	@Autowired
	private AdminService admiService;
	
	@PutMapping("/reiniciarLimiteDiario")
	public ResponseEntity<String> actualizarLimiteDeDineroAlDia() {
		admiService.actualizarLimiteDiaroDeSaldo();
		return new ResponseEntity<>("Limite de dinero reiniciado exitosamente", HttpStatus.OK);
	}

	@PutMapping("/deshabilitarCuentas")
	public ResponseEntity<String> inhabilitarTodasLasCuentas() {
		admiService.inhabilitarTodasLasCuenta();
		return new ResponseEntity<>("Cuentas inhabilitadas correctamentes", HttpStatus.OK);
	}

	@PutMapping("/habilitarCuentas")
	public ResponseEntity<String> habilitarTodasLasCuentas() {
		admiService.habilitarTodasLasCuentas();
		return new ResponseEntity<>("Cuentas habilitadas correctamente", HttpStatus.OK);
	}

	@PutMapping("/aprobarPrestamo/banco/{bancoId}/cuenta/{cuentaId}/prestamo/{prestamoId}")
	public ResponseEntity<String> aprobarPrestamo(@PathVariable(name = "bancoId") long bancoId,@PathVariable(name = "cuentaId") UUID cuentaId, @PathVariable(name = "prestamoId") long prestamoId) {
		admiService.aprobarPrestamo(bancoId, cuentaId, prestamoId);
		return new ResponseEntity<>("Prestamo aprobado exitosamente", HttpStatus.OK);
	}

	@PutMapping("/rechazarPrestamo/banco/{bancoId}/cuenta/{cuentaId}/prestamo/{prestamoId}")
	public ResponseEntity<String> rechazarPrestamo(@PathVariable(name = "bancoId") long bancoId,@PathVariable(name = "cuentaId") UUID cuentaId, @PathVariable(name = "prestamoId") long prestamoId) {
		admiService.rechazarPrestamo(bancoId, cuentaId, prestamoId);
		return new ResponseEntity<>("Prestamo rechzado exitosamente", HttpStatus.OK);
	}

	@PutMapping("/deshabilitarCuentaPorAdmin/banco/{bancoId}/cuenta/{cuentaId}")
	public ResponseEntity<String> inhabilitarCuentaPorAdmin(@PathVariable(name = "bancoId") long bancoId,
			@PathVariable(name = "cuentaId") UUID cuentaId) {
		admiService.deshabilitarCuentaPorAdmin(bancoId, cuentaId);
		return new ResponseEntity<>("Cuenta inhabilitada por admin con exito", HttpStatus.OK);
	}

	@PutMapping("/habilitarCuentaPorAdmin/banco/{bancoId}/cuenta/{cuentaId}")
	public ResponseEntity<String> habilitarCuentaPorAdmin(@PathVariable(name = "bancoId") long bancoId,@PathVariable(name = "cuentaId") UUID cuentaId) {
		admiService.habilitarCuentaPorAdmin(bancoId, cuentaId);
		return new ResponseEntity<>("Cuenta abilitada por admin con exito", HttpStatus.OK);
	}
	
	@GetMapping("/listarTodosLosPrestamos")
	public ResponseEntity<List<PrestamoDTO>> listarTodosLosPrestamos(){
		List<PrestamoDTO> prestamos = admiService.listarPrestamos();
		return ResponseEntity.ok(prestamos);
	}
	
	@GetMapping("/listarCuentas/banco/{bancoId}")
	public ResponseEntity<List<CuentaDTO>> listarCuentasPorBanco(@PathVariable(name = "bancoId")long bancoId){
		List<CuentaDTO> cuentas = admiService.listarCuentasPorBancoId(bancoId);
		return ResponseEntity.ok(cuentas);
	}

	@GetMapping("/listarCuentas/banco/{bancoId}/tipoCuenta/{tipoCuentaId}")
	public ResponseEntity<List<CuentaDTO>> listarCuentasPorTipo(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "tipoCuentaId")long tipoCuentaId){
		List<CuentaDTO> cuentas =admiService.listarCuentasPorTipo(bancoId, tipoCuentaId);
		return ResponseEntity.ok(cuentas);
	
	}
	
	@DeleteMapping("/eliminarCuenta/banco/{bancoId}/cuenta/{cuentaId}")
	public ResponseEntity<String> eliminarCuenta (@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "cuentaId")UUID cuentaId){
		admiService.eliminarCuenta(bancoId, cuentaId);
		return new ResponseEntity<>("Cuenta eliminada con exito",HttpStatus.OK);
	}
	
	@GetMapping("/listarSolicitudesPendientes/banco/{bancoId}")
	public ResponseEntity<List<SolicitudHabilitarCuentaDTO>> listarSolicitudesPendientesParaHabilitar(@PathVariable(name = "bancoId")long bancoId){
		List<SolicitudHabilitarCuentaDTO> solicitudes = admiService.listarSolicitudesPendientes(bancoId);
		return ResponseEntity.ok(solicitudes);
	}
	
	@GetMapping("/listarSolicitudesAprobadas/banco/{bancoId}")
	public ResponseEntity<List<SolicitudHabilitarCuentaDTO>> listarSolicitudesAprobadassParaHabilitar(@PathVariable(name = "bancoId")long bancoId){
		List<SolicitudHabilitarCuentaDTO> solicitudes = admiService.listarSolicitudesAprobadas(bancoId);
		return ResponseEntity.ok(solicitudes);
	}
	
	@GetMapping("/listarSolicitudesRechazadas/banco/{bancoId}")
	public ResponseEntity<List<SolicitudHabilitarCuentaDTO>> listarSolicitudesRechzadasParaHabilitar(@PathVariable(name = "bancoId")long bancoId){
		List<SolicitudHabilitarCuentaDTO> solicitudes = admiService.listarSolicitudesRechazadas(bancoId);
		return ResponseEntity.ok(solicitudes);
	}
	
	@GetMapping("/buscarSolicitud/banco/{bancoId}/solicitud/{solicitudId}")
	public ResponseEntity<SolicitudHabilitarCuentaDTO> buscarSolicitud(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "solicitudId")long solicitudId){
		SolicitudHabilitarCuentaDTO solicitud = admiService.buscarSolicitud(bancoId, solicitudId);
		return ResponseEntity.ok(solicitud);
	}
}
