package com.banco.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.backend.dto.PrestamoDTO;
import com.banco.backend.service.PrestamoService;

@RestController
@RequestMapping("/api")
public class PrestamoController {

	@Autowired
	private PrestamoService prestamoService;
	
	
	@GetMapping("/listarPrestamo/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<List<PrestamoDTO>> listarPrestamoPorUsuario(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId){
		List<PrestamoDTO> prestamos = prestamoService.listarPrestamosPorUsuario(bancoId, usuarioId, cuentaId);
		return ResponseEntity.ok(prestamos);
	}
	
	@PostMapping("/solicitarPrestamo/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<PrestamoDTO> listarPrestamoPorUsuario(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId,@RequestBody PrestamoDTO prestamoDTO){
		PrestamoDTO prestamo = prestamoService.generarPrestamo(bancoId, usuarioId, cuentaId, prestamoDTO);
		return new ResponseEntity<>(prestamo,HttpStatus.CREATED);
	}
	
	@PutMapping("/aprobarPrestamo/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<String> aprobarPrestamo(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId){
		prestamoService.aprobarPrestamo(bancoId, cuentaId, usuarioId);
		return new ResponseEntity<>("Prestamo aprobado exitosamente",HttpStatus.OK);
	}
	
	@PutMapping("/rechazarPrestamo/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<String> rechazarPrestamo(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId){
		prestamoService.rechazarPrestamo(bancoId, cuentaId, usuarioId);
		return new ResponseEntity<>("Prestamo rechzado exitosamente",HttpStatus.OK);
	}
	
		
}