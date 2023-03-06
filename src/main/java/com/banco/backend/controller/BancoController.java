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

import com.banco.backend.dto.BancoDTO;
import com.banco.backend.service.BancoService;

@RestController
@RequestMapping("/api/banco")
public class BancoController {

	@Autowired
	private BancoService service;
	
	@GetMapping
	public ResponseEntity<List<BancoDTO>> listarBancos(){
		List<BancoDTO> bancos = service.listarBancos();
		return ResponseEntity.ok(bancos);
	}
	
	@GetMapping("/{bancoId}")
	public ResponseEntity<BancoDTO> buscarBancoPorId(@PathVariable(name = "bancoId")long bancoId){
		BancoDTO banco = service.buscarBancoPorId(bancoId);
		return ResponseEntity.ok(banco);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/crearBanco")
	public ResponseEntity<BancoDTO> crearBanco(@Valid @RequestBody BancoDTO bancoDTO){
		BancoDTO banco = service.crearBanco(bancoDTO);
		return new ResponseEntity<>(banco,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{bancoId}")
	public ResponseEntity<String> actualizarBanco(@PathVariable(name = "bancoId")long bancoId,@Valid @RequestBody BancoDTO bancoDTO){
		service.actualizarBanco(bancoId, bancoDTO);
		return new ResponseEntity<>("Banco actualizado con exito",HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{bancoId}")
	public ResponseEntity<String> eliminarBanco(@PathVariable(name = "bancoId")long bancoId){
		service.eliminarBanco(bancoId);
		return new ResponseEntity<>("Banco eliminado con exito",HttpStatus.NO_CONTENT);
	}
}
