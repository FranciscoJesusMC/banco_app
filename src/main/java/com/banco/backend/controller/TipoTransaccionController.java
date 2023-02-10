package com.banco.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.backend.dto.TipoTransaccionDTO;
import com.banco.backend.service.TipoTransaccionService;

@RestController
@RequestMapping("/api/tipoTransaccion")
public class TipoTransaccionController {

	@Autowired
	private TipoTransaccionService tipoTransaccionService;
	
	
	@GetMapping
	public ResponseEntity<List<TipoTransaccionDTO>> listarTiposDetransaccion(){
		List<TipoTransaccionDTO> tipos = tipoTransaccionService.listarTiposDeTransaccion();
		return ResponseEntity.ok(tipos);
	}
	
	@PostMapping
	public ResponseEntity<TipoTransaccionDTO> crearTipoDeTransaccion(@RequestBody TipoTransaccionDTO tipoTransaccionDTO){
		TipoTransaccionDTO tipo = tipoTransaccionService.crearTipoDeTransaccion(tipoTransaccionDTO);
		return new ResponseEntity<>(tipo,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{tipoTransaccionId}")
	public ResponseEntity<String> eliminarTipoDeTransaccion(@PathVariable(name = "tipoTransaccionId")long tipoTransaccionId){
		tipoTransaccionService.eliminarTipoDeTransaccion(tipoTransaccionId);
		return new ResponseEntity<>("Tipo de transaccion eliminada con exito",HttpStatus.OK);
		
	}
}
