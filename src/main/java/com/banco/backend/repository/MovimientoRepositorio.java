package com.banco.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.Movimiento;

public interface MovimientoRepositorio extends JpaRepository<Movimiento, Long>  {

	public List<Movimiento> findByCuentaId(UUID cuentaId);
	
	public List<Movimiento> findByTipoTransaccionId(long tipoTransaccionId);
	
	public List<Movimiento> findAllByCuentaIdOrderByFechaCreacionDesc(UUID cuentaId);
}
