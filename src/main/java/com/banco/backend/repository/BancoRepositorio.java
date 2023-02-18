package com.banco.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.Banco;

public interface BancoRepositorio extends JpaRepository<Banco, Long> {

	Optional<Banco> findByNombre(String nombre);
}
