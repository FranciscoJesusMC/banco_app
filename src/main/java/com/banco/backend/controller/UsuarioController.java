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
	
	
	@GetMapping("/listarUsuarios")
	public ResponseEntity<List<UsuarioDTO>> listarUsuariosPorBanco(){
		List<UsuarioDTO> usuarios = usuarioService.listarUsuariosPorBanco();
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/buscarUsuario/{usuarioId}")
	public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable(name = "usuarioId")long usuarioId){
		UsuarioDTO usuario = usuarioService.buscarUsuario(usuarioId);
		return ResponseEntity.ok(usuario);
	}
	
	@PostMapping("/crearUsuario")
	public ResponseEntity<UsuarioDTO> crearUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO){
		UsuarioDTO usuario = usuarioService.crearUsuario(usuarioDTO);
		return new ResponseEntity<>(usuario,HttpStatus.CREATED);
	}
	
	@PutMapping("/actualizarUsuario/{usuarioId}")
	public ResponseEntity<String> actualizarUsuario(@PathVariable(name = "usuarioId")long usuarioId,@Valid @RequestBody UsuarioDTO usuarioDTO){
		usuarioService.actualizarUsuario(usuarioId, usuarioDTO);
		return new ResponseEntity<>("Usuario actualizado con exito",HttpStatus.OK);
	}
	
	@DeleteMapping("/eliminarUsuario/{usuarioId}")
	public ResponseEntity<String> eliminarUsuario(@PathVariable(name = "usuarioId")long usuarioId){
		usuarioService.eliminarUsuario( usuarioId);
		return new ResponseEntity<>("Usuario eliminado con exito",HttpStatus.NO_CONTENT);
	}

}
