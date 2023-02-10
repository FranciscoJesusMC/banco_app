package com.banco.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

	public List<Usuario> findByBancoId(long bancoId);
	
	public boolean existsByEmail(String email);

//	public Usuario merge(Usuario usuario);
}
