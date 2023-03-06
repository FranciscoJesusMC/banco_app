package com.banco.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.backend.dto.RolDTO;
import com.banco.backend.service.RolService;
@RestController
@RequestMapping("/api/rol")
public class RolController {

	@Autowired
	private RolService rolService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<List<RolDTO>> listarRoles(){
		List<RolDTO> roles = rolService.listarRoles();
		return ResponseEntity.ok(roles);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<RolDTO> crearRol(@RequestBody RolDTO rolDTO){
		RolDTO rol = rolService.crearRol(rolDTO);
		return new ResponseEntity<>(rol,HttpStatus.OK);
	}
}
