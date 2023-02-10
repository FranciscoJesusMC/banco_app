package com.banco.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.backend.dto.MovimientoDTO;
import com.banco.backend.dto.TransferenciaDTO;
import com.banco.backend.service.MovimientoService;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {
	
	@Autowired
	private MovimientoService movimientosService;
	
	@GetMapping("listar/banco/{bancoId}/cuenta/{cuentaId}")
	public ResponseEntity<List<MovimientoDTO>> listarMovimientosPorCuentaId(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "cuentaId")UUID cuentaId){
		List<MovimientoDTO> movimientos = movimientosService.listarMovimientosPorCuenta(bancoId,cuentaId);
		return ResponseEntity.ok(movimientos);
	}
	
	@PutMapping("/agregarSaldo/banco/{bancoId}/cuenta/{cuentaId}")
	public ResponseEntity<MovimientoDTO> agregarSaldo(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "cuentaId")UUID cuentaId,@RequestBody MovimientoDTO movimientosDTO){
		MovimientoDTO movimiento = movimientosService.agregarSaldo(bancoId, cuentaId, movimientosDTO);
		return new ResponseEntity<>(movimiento,HttpStatus.OK);
	}
	
	@PutMapping("/retiro/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<MovimientoDTO> retiro(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId,@RequestBody MovimientoDTO movimientosDTO){
		MovimientoDTO movimiento = movimientosService.retiro(bancoId, usuarioId,cuentaId, movimientosDTO);
		return new ResponseEntity<>(movimiento,HttpStatus.OK);
	}
	
	@PutMapping("/transferenciaBancaria/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<MovimientoDTO> transferenciaBancaria(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId,@RequestBody TransferenciaDTO transferenciaDTO){
		MovimientoDTO movimiento = movimientosService.transferenciaBancaria(bancoId,usuarioId, cuentaId, transferenciaDTO);
		return new ResponseEntity<>(movimiento,HttpStatus.OK);
	}

	@PutMapping("/transferenciaInterbancaria/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}")
	public ResponseEntity<MovimientoDTO> transferenciaInterbancaria(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@PathVariable(name = "cuentaId")UUID cuentaId,@RequestBody TransferenciaDTO transferenciaDTO){
		MovimientoDTO movimiento = movimientosService.transferenciaInterbancaria(bancoId,usuarioId, cuentaId, transferenciaDTO);
		return new ResponseEntity<>(movimiento,HttpStatus.OK);
	}

}
