package com.banco.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.TipoTransaccion;

public interface TipoTransaccionRepositorio extends JpaRepository<TipoTransaccion, Long> {
	
	public Optional<TipoTransaccion> findByNombre(String nombre);
}
