package com.banco.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.Banco;

public interface BancoRepositorio extends JpaRepository<Banco, Long> {

}
