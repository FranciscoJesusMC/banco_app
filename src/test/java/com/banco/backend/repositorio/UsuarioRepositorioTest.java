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

import com.banco.backend.entity.Usuario;
import com.banco.backend.repository.UsuarioRepositorio;

@DataJpaTest
public class UsuarioRepositorioTest {

	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	private Usuario usuario;
	
	private Usuario usuario2;
	
	@BeforeEach
	void init() {
		
		usuario = new Usuario();
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");
		
		usuario2 = new Usuario();
		usuario2.setNombre("Juan");
		usuario2.setApellido("Maculunco");
		usuario2.setCelular("987776029");
		usuario2.setDni("76337194");
		usuario2.setEdad(18);
		usuario2.setEmail("Juan@gmail.com");
	}
	
	@Test
	void guardar() {
		
		Usuario guardarUsuario = usuarioRepositorio.save(usuario);
		
		assertNotNull(guardarUsuario);
		assertThat(guardarUsuario.getId()).isNotEqualTo(0);
		
	}
	
	@Test
	void listar() {
		
		usuarioRepositorio.save(usuario);
		usuarioRepositorio.save(usuario2);
		
		List<Usuario> lista = usuarioRepositorio.findAll();
		
		assertNotNull(lista);
		assertEquals(2, lista.size());
	}
	
	@Test
	void buscarUsuario() {
		
		usuarioRepositorio.save(usuario);
		
		Usuario buscarUsuario = usuarioRepositorio.findById(usuario.getId()).get();
		
		assertNotNull(buscarUsuario);
		assertEquals("Francisco", buscarUsuario.getNombre());
		
	}
	
	@Test
	void actualizarUsuario() {
		
		usuarioRepositorio.save(usuario);
		
		Usuario buscarUsuario = usuarioRepositorio.findById(usuario.getId()).get();
		buscarUsuario.setNombre("Alberto");
		
		Usuario actualizar = usuarioRepositorio.save(buscarUsuario);
		
		assertEquals("Alberto", actualizar.getNombre());
		
	}
	
	@Test
	void eliminarUsuario() {
		
		usuarioRepositorio.save(usuario);
		usuarioRepositorio.save(usuario2);
		
		usuarioRepositorio.delete(usuario);
		Optional<Usuario> buscar = usuarioRepositorio.findById(usuario.getId());
		List<Usuario> lista = usuarioRepositorio.findAll();
		
		
		assertEquals(1, lista.size());
		assertThat(buscar).isEmpty();
		
	
	}
		
	
}
