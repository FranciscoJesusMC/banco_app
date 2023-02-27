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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.service.CuentaService;

@RestController
@RequestMapping("/api")
public class CuentaController {
	
	@Autowired
	private CuentaService cuentaService;
	
	@GetMapping("/listarCuentas/banco/{bancoId}/usuario/{usuarioId}")
	public ResponseEntity<List<CuentaDTO>> listarCuentasDelUsuarioPorBanco(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId){
		List<CuentaDTO> cuentas = cuentaService.listarCuentasPorUsuarioId(bancoId, usuarioId);
		return ResponseEntity.ok(cuentas);
	}
	
	@GetMapping("/buscarCuenta/banco/{bancoId}/cuenta/{cuentaId}")
	public ResponseEntity<CuentaDTO> buscarCuenta (@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "cuentaId")UUID cuentaId){
		CuentaDTO cuenta = cuentaService.buscarCuentaPorId(bancoId, cuentaId);
		return ResponseEntity.ok(cuenta);
	}
	@PostMapping("/crearCuenta/banco/{bancoId}/usuario/{usuarioId}/tipoCuenta/{tipoCuentaId}")
	public ResponseEntity<CuentaDTO> crearCuenta(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "tipoCuentaId")long tipoCuentaId){
		CuentaDTO cuenta = cuentaService.crearCuenta(bancoId, usuarioId, tipoCuentaId);
		return new ResponseEntity<>(cuenta,HttpStatus.CREATED);
	}
	
	@PutMapping("/deshabilitarCuenta/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<String> inhabilitarCuenta (@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId){
		cuentaService.deshabilitarCuentaPorUsuario(bancoId, usuarioId, cuentaId);
		return new ResponseEntity<>("Cuenta deshabilitada con exito",HttpStatus.OK);
	}
	
	
}
