package com.banco.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banco.backend.dto.BancoDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.serviceImpl.BancoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BancoServiceTest {
	
	@Mock
	private BancoRepositorio bancoRepositorio;
	
	@InjectMocks
	private BancoServiceImpl bancoService;
	
	private Banco banco;
	
	private Banco banco2;
	
	private BancoDTO bancoDTO;
	
	@BeforeEach
	void init() {
		
		banco = new Banco();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
		
		banco2 = new Banco();
		banco2.setId(2L);
		banco2.setNombre("Banco nuevo2");
		
		bancoDTO = new BancoDTO();
		banco.setNombre("Banco nuevo");
	}

	
	@DisplayName("Test para crear un banco")
	@Test
	void crearbanco() {
		
		when(bancoRepositorio.save(any(Banco.class))).thenReturn(banco);
		
		BancoDTO resultado = bancoService.crearBanco(bancoDTO);
		
		assertNotNull(resultado);
		verify(bancoRepositorio,times(1)).save(any(Banco.class));
		assertEquals(banco.getNombre(), resultado.getNombre());
			
		
	}
	
	@DisplayName("Test para buscar un banco")
	@Test
	void buscarBanco() {

		
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		
		BancoDTO resultado = bancoService.buscarBancoPorId(1L);
		
		assertThat(resultado);
		assertEquals("Banco nuevo", resultado.getNombre());
		
		
		
	}
	
	@DisplayName("Test para listar los bancos")
	@Test
	void listarBanco() {
	
		List<Banco> lista  = new ArrayList<>();
		lista.add(banco);
		lista.add(banco2);
		
		when(bancoRepositorio.findAll()).thenReturn(lista);
		
		List<BancoDTO> listaDTO = bancoService.listarBancos();
		
		assertNotNull(listaDTO);
		assertEquals(2, listaDTO.size());
	}
	
	
	@DisplayName("Test para actualizar un banco")
	@Test
	void actualizarBanco() {
		
		Banco banco = new Banco();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
		
		BancoDTO bancoDTO = new BancoDTO();
		bancoDTO.setNombre("Banco actualizado");
		
		
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		when(bancoRepositorio.save(any(Banco.class))).thenReturn(banco);
		banco.setNombre(bancoDTO.getNombre());
		
		BancoDTO resultado = bancoService.actualizarBanco(1L, bancoDTO);
		
		assertNotNull(resultado);
		assertEquals("Banco actualizado", resultado.getNombre());
		
	}
	
	@DisplayName("Test para eliminar un banco")
	@Test 
	void eliminarBanco() {
		
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		doNothing().when(bancoRepositorio).delete(any(Banco.class));
		
		bancoService.eliminarBanco(1L);
		
		verify(bancoRepositorio,times(1)).delete(banco);
		
	
	}
}
