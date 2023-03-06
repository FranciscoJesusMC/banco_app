package com.banco.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
	
	public boolean existsByEmail(String email);

	public boolean existsByDni(String dni);
	
	public boolean existsByCelular(String celular);
	
	public boolean existsByUsername(String username);
	
	public Optional<Usuario> findByUsernameOrEmail(String username, String email);
	
	public Usuario findByUsername(String username);
	
	public Usuario findByEmail(String email);
}
