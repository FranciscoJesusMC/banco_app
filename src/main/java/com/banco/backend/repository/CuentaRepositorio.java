package com.banco.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banco.backend.entity.Cuenta;

@Repository
public interface CuentaRepositorio extends JpaRepository<Cuenta, UUID> {

	public List<Cuenta> findByUsuarioId(long usuarioId);
	
	public List<Cuenta> findByBancoId(long bancoId);
	
	public List<Cuenta> findByTipoCuentaId(long tipoCuentaid);
	
	public List<Cuenta> findByUsuarioIdAndBancoId(long usuarioId,long bancoId);
	
	public Optional<Cuenta> findByCci(long cci);
	

}
