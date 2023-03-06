package com.banco.backend.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.backend.dto.TipoCuentaDTO;
import com.banco.backend.service.TipoCuentaService;

@RestController
@RequestMapping("/api/tipoCuenta")
public class TipoCuentaController {
	
	@Autowired
	private TipoCuentaService tipoCuentaService;
	
	
	@GetMapping
	public ResponseEntity<List<TipoCuentaDTO>> listarTiposDeCuenta(){
		List<TipoCuentaDTO> cuentas = tipoCuentaService.listarTiposDecuenta();
		return ResponseEntity.ok(cuentas);
	}
	
	@GetMapping("/{tipoCuentaId}")
	public ResponseEntity<TipoCuentaDTO> buscarTipoDeCuentaPorId(@PathVariable(name = "tipoCuentaId")long tipoCuentaId){
		TipoCuentaDTO tipoCuenta = tipoCuentaService.buscarTipoCuentaPorId(tipoCuentaId);
		return ResponseEntity.ok(tipoCuenta);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<TipoCuentaDTO> crearTipodeCuenta(@RequestBody TipoCuentaDTO tipoCuentaDTO){
		TipoCuentaDTO tipoCuenta = tipoCuentaService.crearTipoCuenta(tipoCuentaDTO);
		return new ResponseEntity<>(tipoCuenta,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{tipoCuentaId}")
	public ResponseEntity<String> actualizarTipoCuenta(@PathVariable(name = "tipoCuentaId")long tipoCuentaId,@Valid @RequestBody TipoCuentaDTO tipoCuentaDTO){
		tipoCuentaService.actualizarTipoCuenta(tipoCuentaId, tipoCuentaDTO);
		return new ResponseEntity<>("Tipo de cuenta actualizada con exito",HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{tipoCuentaId}")
	public ResponseEntity<String> eliminarTipoCuenta(@PathVariable(name = "tipoCuentaId")long tipoCuentaId){
		tipoCuentaService.eliminarTipoCuenta(tipoCuentaId);
		return new ResponseEntity<>("Tipo de cuenta eliminado con exito",HttpStatus.OK);
	}

}
