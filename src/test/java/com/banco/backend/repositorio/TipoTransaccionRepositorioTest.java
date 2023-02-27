package com.banco.backend.repositorio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.banco.backend.entity.TipoTransaccion;
import com.banco.backend.repository.TipoTransaccionRepositorio;

@DataJpaTest
public class TipoTransaccionRepositorioTest {

	@Autowired
	private TipoTransaccionRepositorio repositorio;
	
	private TipoTransaccion tipo;
	
	private TipoTransaccion tipo2;
	
	@BeforeEach
	void init() {
		
		tipo = new TipoTransaccion();
		tipo.setNombre("Bancaria");
		
		tipo2 = new TipoTransaccion();
		tipo2.setNombre("Interbancaria");
		
	}
	
	@Test
	void crearTipoCuenta() {
		
		TipoTransaccion crear = repositorio.save(tipo);
		
		assertNotNull(crear);
		assertEquals("Bancaria", crear.getNombre());
	}
	
	@Test
	void buscarTipoCuenta() {
		
		repositorio.save(tipo);
		
		TipoTransaccion buscarTipo = repositorio.findById(tipo.getId()).get();
		
		assertNotNull(buscarTipo);
		assertThat(buscarTipo.getId()).isNotEqualTo(0);
		
		
	}
	
	@Test
	void listarTiposDeCuenta() {
		
		repositorio.save(tipo);
		repositorio.save(tipo2);
		
		List<TipoTransaccion> lista = repositorio.findAll();
		
		assertNotNull(lista);
		assertEquals(2, lista.size());
		
		
	}
	
	@Test
	void actualizarTipoCuenta() {
		
		repositorio.save(tipo);
		
		TipoTransaccion buscar = repositorio.findById(tipo.getId()).get();
		buscar.setNombre("Cuenta actualizada");
		
		TipoTransaccion actualizar = repositorio.save(buscar);
		
		assertNotNull(actualizar);
		assertEquals("Cuenta actualizada", actualizar.getNombre());
	}
	
	@Test
	void eliminarTipoCuenta() {
		
		repositorio.save(tipo);
		repositorio.save(tipo2);
		
		repositorio.delete(tipo);
		
		Optional<TipoTransaccion> buscar = repositorio.findById(tipo.getId());
		List<TipoTransaccion> lista = repositorio.findAll();
		
		assertEquals(1, lista.size());
		assertThat(buscar).isEmpty();
		
	}
}
