package com.banco.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.Rol;

public interface RolRepositorio extends JpaRepository<Rol, Long> {
	
	public Optional<Rol> findByNombre(String nombre);

}
