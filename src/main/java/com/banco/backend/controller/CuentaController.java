package com.banco.backend.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.service.CuentaService;

@RestController
@RequestMapping("/api")
public class CuentaController {
	
	@Autowired
	private CuentaService cuentaService;
	
	@GetMapping("/listarCuentas/banco/{bancoId}")
	public ResponseEntity<List<CuentaDTO>> listarCuentasPorBanco(@PathVariable(name = "bancoId")long bancoId){
		List<CuentaDTO> cuentas = cuentaService.listarCuentasPorBancoId(bancoId);
		return ResponseEntity.ok(cuentas);
	}
	
	@GetMapping("/listarCuentas/banco/{bancoId}/usuario/{usuarioId}")
	public ResponseEntity<List<CuentaDTO>> listarCuentasDelUsuarioPorBanco(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId){
		List<CuentaDTO> cuentas = cuentaService.listarCuentasPorUsuarioId(bancoId, usuarioId);
		return ResponseEntity.ok(cuentas);
	}
	
	@GetMapping("/listarCuentas/banco/{bancoId}/tipoCuenta/{tipoCuentaId}")
	public ResponseEntity<List<CuentaDTO>> listarCuentasPorTipo(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "tipoCuentaId")long tipoCuentaId){
		List<CuentaDTO> cuentas =cuentaService.listarCuentasPorTipo(bancoId, tipoCuentaId);
		return ResponseEntity.ok(cuentas);
	}
	
	@PostMapping("/crearCuenta/banco/{bancoId}/usuario/{usuarioId}/tipoCuenta/{tipoCuentaId}")
	public ResponseEntity<CuentaDTO> crearCuenta(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "tipoCuentaId")long tipoCuentaId,@Valid @RequestBody CuentaDTO cuentaDTO){
		CuentaDTO cuenta = cuentaService.crearCuenta(bancoId, usuarioId, tipoCuentaId, cuentaDTO);
		return new ResponseEntity<>(cuenta,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/eliminarCuenta/banco/{bancoId}/cuenta/{cuentaId}")
	public ResponseEntity<String> eliminarCuenta (@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "cuentaId")UUID cuentaId){
		cuentaService.eliminarCuenta(bancoId, cuentaId);
		return new ResponseEntity<>("Cuenta eliminada con exito",HttpStatus.OK);
	}
	
	@PutMapping("/inhabilitarCuenta/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<String> inhabilitarCuenta (@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "cuentaId")UUID cuentaId){
		cuentaService.deshabilitarCuentaPorUsuario(bancoId, bancoId, cuentaId);
		return new ResponseEntity<>("Cuenta inhabilitada con exito",HttpStatus.OK);
	}
	
	@PutMapping("/inhabilitarCuentaPorAdmin/banco/{bancoId}/cuenta/{cuentaId}")
	public ResponseEntity<String> inhabilitarCuentaPorAdmin(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "cuentaId")UUID cuentaId){
		cuentaService.deshabilitarCuentaPorAdmin(bancoId, cuentaId);
		return new ResponseEntity<>("Cuenta inhabilitada por admin con exito",HttpStatus.OK);
	}
	
	@PutMapping("/habilitarCuenta/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<String> habilitarCuentaPorUsuario(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId){
		cuentaService.habilitarCuentaPorUsuario(bancoId, usuarioId, cuentaId);
		return new ResponseEntity<>("Cuenta habilitada con exito",HttpStatus.OK);
	}
	
	@PutMapping("/habilitarCuentaPorAdmin/banco/{bancoId}/cuenta/{cuentaId}")
	public ResponseEntity<String> habilitarCuentaPorAdmin(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "cuentaId")UUID cuentaId){
		cuentaService.habilitarCuentaPorAdmin(bancoId, cuentaId);
		return new ResponseEntity<>("Cuenta abilitada por admin con exito",HttpStatus.OK);
	}

}
