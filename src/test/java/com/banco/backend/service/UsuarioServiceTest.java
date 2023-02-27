package com.banco.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banco.backend.dto.UsuarioDTO;
import com.banco.backend.entity.Usuario;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.serviceImpl.UsuarioServiceImpl;
import com.banco.backend.utils.Genero;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

	@Mock
	private UsuarioRepositorio usuarioRepositorio;
	
	@InjectMocks
	private UsuarioServiceImpl usuarioService;
	
	@Test
	void crear() {
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setNombre("Francisco");
		usuarioDTO.setApellido("Maculunco");
		usuarioDTO.setCelular("987776029");
		usuarioDTO.setDni("76337194");
		usuarioDTO.setGenero("M");
		usuarioDTO.setEdad(18);
		usuarioDTO.setEmail("franck@gmail.com");
		
		when(usuarioRepositorio.save(any(Usuario.class))).thenReturn(usuario);

		
		UsuarioDTO resultado = usuarioService.crearUsuario(usuarioDTO);
		
		assertNotNull(resultado);
		verify(usuarioRepositorio,times(1)).save(any(Usuario.class));
		assertEquals(usuario.getNombre(), resultado.getNombre());
	}
	
	@Test
	void buscarUsuario() {
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");
		
		when(usuarioRepositorio.findById(1L)).thenReturn(Optional.of(usuario));
		
		UsuarioDTO resultado = usuarioService.buscarUsuario(1L);
		
		assertNotNull(resultado);
		assertEquals("Francisco", resultado.getNombre());

		
	}
	
	@Test
	void listarUsuarios() {
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");
		
		Usuario usuario2 = new Usuario();
		usuario2.setId(1L);
		usuario2.setNombre("Julio");
		usuario2.setApellido("De la vega");
		usuario2.setCelular("123456789");
		usuario2.setDni("94713376");
		usuario2.setGenero(Genero.M);
		usuario2.setEdad(18);
		usuario2.setEmail("julio@gmail.com");
		
		List<Usuario> lista = new ArrayList<>();
		lista.add(usuario);
		lista.add(usuario2);
		
		when(usuarioRepositorio.findAll()).thenReturn(lista);
		
		List<UsuarioDTO> listaDTO = usuarioService.listarUsuariosPorBanco();
		
		assertNotNull(listaDTO);
		assertEquals(2, listaDTO.size());
		
	}
	
	@Test
	void actualizarUsuario() {
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");
		
		UsuarioDTO usuario2 = new UsuarioDTO();
		usuario2.setNombre("Julio");
		usuario2.setApellido("De la vega");
		usuario2.setCelular("123456789");
		usuario2.setDni("94713376");
		usuario2.setGenero("M");
		usuario2.setEdad(18);
		usuario2.setEmail("julio@gmail.com");
		
		when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.of(usuario));
		when(usuarioRepositorio.save(any(Usuario.class))).thenReturn(usuario);
		usuario.setNombre(usuario2.getNombre());
		usuario.setApellido(usuario2.getApellido());
		
		UsuarioDTO resultado = usuarioService.actualizarUsuario(1L, usuario2);
		
		assertNotNull(resultado);
		assertEquals("Julio", resultado.getNombre());
		assertEquals("De la vega", resultado.getApellido());
		
		
	}
	
	@Test
	void eliminarUsuario() {
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");
		
		when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.of(usuario));
		doNothing().when(usuarioRepositorio).delete(any(Usuario.class));
		
		usuarioService.eliminarUsuario(1L);
		
		verify(usuarioRepositorio,times(1)).delete(usuario);
		
	}
}
