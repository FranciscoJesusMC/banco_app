package com.banco.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banco.backend.entity.Banco;

@Repository
public interface BancoRepositorio extends JpaRepository<Banco, Long> {

	Optional<Banco> findByNombre(String nombre);
}
