package com.banco.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.DetalleMovimiento;

public interface DetalleMovimientoRepositorio extends JpaRepository<DetalleMovimiento, Long> {

}
