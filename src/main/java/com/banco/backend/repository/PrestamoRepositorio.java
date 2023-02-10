package com.banco.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.Prestamo;

public interface PrestamoRepositorio  extends JpaRepository<Prestamo, Long>{
	
	List<Prestamo> findByCuentaId(UUID cuentaId);

}
