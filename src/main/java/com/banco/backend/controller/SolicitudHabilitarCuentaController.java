package com.banco.backend.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.backend.dto.SolicitudHabilitarCuentaDTO;
import com.banco.backend.service.SolicitudHabilitarCuentaService;

@RestController
@RequestMapping("/api")
public class SolicitudHabilitarCuentaController {

	@Autowired
	private SolicitudHabilitarCuentaService service;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/misSolicitudes/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<List<SolicitudHabilitarCuentaDTO>> misSolicitudes(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId){
		List<SolicitudHabilitarCuentaDTO> solicitudes = service.listarSolicitudes(bancoId, usuarioId, cuentaId);
		return ResponseEntity.ok(solicitudes);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/crearSolicitud/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<SolicitudHabilitarCuentaDTO> generarSolicitud(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId, @Valid @RequestBody SolicitudHabilitarCuentaDTO solicitudHabilitarCuentaDTO){
		SolicitudHabilitarCuentaDTO solicitud = service.crearSolicitudHabilitarCuenta(bancoId, usuarioId, cuentaId, solicitudHabilitarCuentaDTO);
		return new ResponseEntity<>(solicitud,HttpStatus.CREATED);
	}
}
