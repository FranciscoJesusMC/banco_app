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

import com.banco.backend.entity.Banco;
import com.banco.backend.repository.BancoRepositorio;

@DataJpaTest
public class BancoRepositorioTest {
	
	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	private Banco banco;
	
	private Banco banco2;
	
	
	@BeforeEach
	void init() {
		
		banco = new Banco();
		banco.setNombre("Banco numero 1");
		
		banco2= new Banco();
		banco2.setNombre("Banco numero 2");
		
	}
	

	@Test
	void crearBanco() {
				
		Banco guardarBanco = bancoRepositorio.save(banco);
		
		assertNotNull(guardarBanco);
		assertThat(guardarBanco.getId()).isNotEqualTo(0);
	}
	
	@Test
	void listarBanco() {
		
		bancoRepositorio.save(banco);
		bancoRepositorio.save(banco2);
		
		List<Banco> lista = bancoRepositorio.findAll();
		
		assertNotNull(lista);
		assertEquals(2, lista.size());
		
	}
	
	@Test
	void buscarBanco() {
		
		bancoRepositorio.save(banco);
		
		Banco nuevoBanco = bancoRepositorio.findById(banco.getId()).get();
		
		assertNotNull(nuevoBanco);
		assertEquals("Banco numero 1", nuevoBanco.getNombre());
	}
	
	@Test
	void actualizarBanco() {
		
		bancoRepositorio.save(banco);
		
		Banco bancoNuevo = bancoRepositorio.findById(banco.getId()).get();
		bancoNuevo.setNombre("Banco actualizado");
		
		Banco bancoActualizado = bancoRepositorio.save(bancoNuevo);
		
		assertNotNull(bancoActualizado);
		assertEquals("Banco actualizado", bancoActualizado.getNombre());
		
	}
	
	@Test
	void eliminarBanco() {
		
		bancoRepositorio.save(banco);
		bancoRepositorio.save(banco2);
		
		bancoRepositorio.delete(banco);
		Optional<Banco> buscarBanco = bancoRepositorio.findById(banco.getId());
		
		List<Banco> lista = bancoRepositorio.findAll();
		
		assertEquals(1, lista.size());
		assertThat(buscarBanco).isEmpty();
		
		
	}

}
