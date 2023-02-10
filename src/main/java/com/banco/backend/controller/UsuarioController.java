package com.banco.backend.controller;

import java.util.List;

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

import com.banco.backend.dto.UsuarioDTO;
import com.banco.backend.service.UsuarioService;

@RestController
@RequestMapping("/api")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	
	@GetMapping("/listarUsuarios/banco/{bancoId}")
	public ResponseEntity<List<UsuarioDTO>> listarUsuariosPorBanco(@PathVariable(name = "bancoId")long bancoId){
		List<UsuarioDTO> usuarios = usuarioService.listarUsuariosPorBanco(bancoId);
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/buscarUsuario/banco/{bancoId}/usuario/{usuarioId}")
	public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId){
		UsuarioDTO usuario = usuarioService.buscarUsuarioPorBancoId(bancoId, usuarioId);
		return ResponseEntity.ok(usuario);
	}
	
	@PostMapping("/crearUsuario/banco/{bancoId}")
	public ResponseEntity<UsuarioDTO> crearUsuario(@PathVariable(name = "bancoId")long bancoId,@Valid @RequestBody UsuarioDTO usuarioDTO){
		UsuarioDTO usuario = usuarioService.crearUsuario(bancoId, usuarioDTO);
		return new ResponseEntity<>(usuario,HttpStatus.CREATED);
	}
	
	@PutMapping("/actualizarUsuario/banco/{bancoId}/usuario/{usuarioId}")
	public ResponseEntity<String> actualizarUsuario(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId,@RequestBody UsuarioDTO usuarioDTO){
		usuarioService.actualizarUsuario(bancoId, usuarioId, usuarioDTO);
		return new ResponseEntity<>("Usuario actualizado con exito",HttpStatus.OK);
	}
	
	@DeleteMapping("/eliminarUsuario/banco/{bancoId}/usuario/{usuarioId}")
	public ResponseEntity<String> eliminarUsuario(@PathVariable(name = "bancoId")long bancoId,@PathVariable(name = "usuarioId")long usuarioId){
		usuarioService.eliminarUsuario(bancoId, usuarioId);
		return new ResponseEntity<>("Usuario eliminado con exito",HttpStatus.OK);
	}

}
