package com.banco.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.SolicitudHabilitarCuenta;

public interface SolicitudHabilitarCuentaRepositorio extends JpaRepository<SolicitudHabilitarCuenta, Long> {
	
	List<SolicitudHabilitarCuenta> findByCuentaId(UUID cuentaId);

}
