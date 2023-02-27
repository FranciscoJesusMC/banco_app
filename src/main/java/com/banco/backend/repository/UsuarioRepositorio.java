package com.banco.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
	
	public boolean existsByEmail(String email);

	public boolean existsByDni(String dni);
	
	public boolean existsByCelular(String celular);
}
