package com.banco.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.TipoCuenta;

public interface TipoCuentaRepositorio extends JpaRepository<TipoCuenta, Long>  {
	
	Optional<TipoCuenta> findByNombre(String nombre);

}
