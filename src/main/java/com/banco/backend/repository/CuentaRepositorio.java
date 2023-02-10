package com.banco.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.backend.entity.Cuenta;

public interface CuentaRepositorio extends JpaRepository<Cuenta, UUID> {

	public List<Cuenta> findByUsuarioId(long usuarioId);
	
	public List<Cuenta> findByBancoId(long bancoId);
	
	public List<Cuenta> findByTipoCuentaId(long tipoCuentaid);
}
