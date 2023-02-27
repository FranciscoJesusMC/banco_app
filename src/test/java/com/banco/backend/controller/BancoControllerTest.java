package com.banco.backend.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.banco.backend.dto.BancoDTO;
import com.banco.backend.serviceImpl.BancoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@Import({BancoController.class,BancoServiceImpl.class})
public class BancoControllerTest {
	
	@MockBean
	private BancoServiceImpl bancoService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private BancoDTO bancoDTO;
	
	private BancoDTO bancoDTO2;
	
	@BeforeEach
	void init() {
		
		bancoDTO = new BancoDTO();
		bancoDTO.setNombre("Banco nuevo");
		
		
		bancoDTO2 = new BancoDTO();
		bancoDTO2.setNombre("Banco nuevo2");
	}
	
	
	@Test
	void crearBanco() throws Exception {
	

		when(bancoService.crearBanco(any(BancoDTO.class))).thenReturn(bancoDTO);
		
		this.mockMvc.perform(post("/api/banco/crearBanco")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bancoDTO)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.nombre", is(bancoDTO.getNombre())));
		
		verify(bancoService,times(1)).crearBanco(any(BancoDTO.class));
		
		
	}
	
	@Test
	void BuscarBanco() throws Exception {
		
		when(bancoService.buscarBancoPorId(anyLong())).thenReturn(bancoDTO);
		
		this.mockMvc.perform(get("/api/banco/{bancoId}",1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bancoDTO)))
		.andExpect(status().isOk());
		
		verify(bancoService,times(1)).buscarBancoPorId(1L);
		
		
	}
	
	@Test
	void listarBancos() throws Exception {
		
		List<BancoDTO> lista = new ArrayList<>();
		lista.add(bancoDTO);
		lista.add(bancoDTO2);
		
		when(bancoService.listarBancos()).thenReturn(lista);
		
		this.mockMvc.perform(get("/api/banco"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.size()", is(lista.size())));
		
	}
	
	@Test
	void actualizarbanco() throws Exception {
		
		BancoDTO bancoDTO = new BancoDTO();
		bancoDTO.setNombre("Banco nuevo");
		
		BancoDTO bancoActualizado = new BancoDTO();
		bancoActualizado.setNombre(bancoDTO.getNombre());
		
		when(bancoService.actualizarBanco(anyLong(), any(BancoDTO.class))).thenReturn(bancoActualizado);
		
		MvcResult result =mockMvc.perform(put("/api/banco/{bancoId}",1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bancoActualizado)))
		.andExpect(status().isOk())
		.andReturn();
		
		String response = result.getResponse().getContentAsString();
		
		//Si devolvemos un objeto
//		BancoDTO bancoResponse = new ObjectMapper().readValue(response, BancoDTO.class);
//		assertEquals(bancoActualizado.getNombre(), bancoResponse.getNombre());
		assertEquals("Banco actualizado con exito",response);
	}
	
	@Test
	void eliminarBanco() throws Exception{
		
		doNothing().when(bancoService).eliminarBanco(anyLong());
		
		this.mockMvc.perform(delete("/api/banco/{bancoId}",1L))
		.andExpect(status().isNoContent());
	}

}
