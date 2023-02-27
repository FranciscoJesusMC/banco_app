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

import com.banco.backend.entity.TipoCuenta;
import com.banco.backend.repository.TipoCuentaRepositorio;

@DataJpaTest
public class TipoCuentaRepositorioTest {

	
	@Autowired
	private TipoCuentaRepositorio tipoRepositorio;
	
	private TipoCuenta tipoCuenta;
	
	private TipoCuenta tipoCuenta2;
	
	@BeforeEach
	void init() {
		
		tipoCuenta = new TipoCuenta();
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");
		
		tipoCuenta2 = new TipoCuenta();
		tipoCuenta2.setNombre("Cuenta de prueba2");
		tipoCuenta2.setDescripcion("hola2");
		
	}
	
	@Test
	void crearTipoCuenta() {
		
		TipoCuenta crear = tipoRepositorio.save(tipoCuenta);
		
		assertNotNull(crear);
		assertEquals("hola", crear.getDescripcion());
	}
	
	@Test
	void buscarTipoCuenta() {
		
		tipoRepositorio.save(tipoCuenta);
		
		TipoCuenta buscarTipo = tipoRepositorio.findById(tipoCuenta.getId()).get();
		
		assertNotNull(buscarTipo);
		assertThat(buscarTipo.getId()).isNotEqualTo(0);
		
		
	}
	
	@Test
	void listarTiposDeCuenta() {
		
		tipoRepositorio.save(tipoCuenta);
		tipoRepositorio.save(tipoCuenta2);
		
		List<TipoCuenta> lista = tipoRepositorio.findAll();
		
		assertNotNull(lista);
		assertEquals(2, lista.size());
		
		
	}
	
	@Test
	void actualizarTipoCuenta() {
		
		tipoRepositorio.save(tipoCuenta);
		
		TipoCuenta buscar = tipoRepositorio.findById(tipoCuenta.getId()).get();
		buscar.setNombre("Cuenta actualizada");
		
		TipoCuenta actualizar = tipoRepositorio.save(buscar);
		
		assertNotNull(actualizar);
		assertEquals("Cuenta actualizada", actualizar.getNombre());
	}
	
	@Test
	void eliminarTipoCuenta() {
		
		tipoRepositorio.save(tipoCuenta);
		tipoRepositorio.save(tipoCuenta2);
		
		tipoRepositorio.delete(tipoCuenta);
		
		Optional<TipoCuenta> buscar = tipoRepositorio.findById(tipoCuenta.getId());
		List<TipoCuenta> lista = tipoRepositorio.findAll();
		
		assertEquals(1, lista.size());
		assertThat(buscar).isEmpty();
		
	}
}
