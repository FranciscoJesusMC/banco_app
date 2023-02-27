package com.banco.backend.controller;

import static org.hamcrest.CoreMatchers.is;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.banco.backend.dto.UsuarioDTO;
import com.banco.backend.serviceImpl.UsuarioServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
@Import({UsuarioController.class, UsuarioServiceImpl.class})
public class UsuarioControllerTest {

	@MockBean
	private UsuarioServiceImpl usuarioService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Test
	void crearUsuario() throws Exception{
		
		UsuarioDTO usuario2 = new UsuarioDTO();
		usuario2.setNombre("Julio");
		usuario2.setApellido("De la vega");
		usuario2.setDni("94713376");
		usuario2.setGenero("M");
		usuario2.setEdad(18);
		usuario2.setEmail("julio@gmail.com");
		usuario2.setCelular("123456789");
		
		
		when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenReturn(usuario2);
		
		this.mockMvc.perform(post("/api/crearUsuario")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(usuario2)))
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.nombre", is(usuario2.getNombre())))
		.andExpect(jsonPath("$.apellido", is(usuario2.getApellido())))
		.andExpect(jsonPath("$.dni", is(usuario2.getDni())))
		.andExpect(jsonPath("$.genero", is(usuario2.getGenero())))
		.andExpect(jsonPath("$.edad", is(usuario2.getEdad())))
		.andExpect(jsonPath("$.email", is(usuario2.getEmail())))
		.andExpect(jsonPath("$.celular", is(usuario2.getCelular())));
		
		
        verify(usuarioService, times(1)).crearUsuario(any(UsuarioDTO.class));
		

	}
	
	
	@Test
	void getUsuarioForId() throws Exception {
		
        UsuarioDTO usuarioExistente = new UsuarioDTO();
        usuarioExistente.setNombre("Juan");
        usuarioExistente.setApellido("Pérez");
        usuarioExistente.setDni("12345678");
        usuarioExistente.setEdad(30);
        usuarioExistente.setGenero("M");
        usuarioExistente.setEmail("juan.perez@gmail.com");
        usuarioExistente.setCelular("123456789");
        
        when(usuarioService.buscarUsuario(anyLong())).thenReturn(usuarioExistente);

        mockMvc.perform(get("/api/buscarUsuario/{usuarioId}",1L)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(usuarioExistente)))
                .andExpect(status().isOk());
            
        verify(usuarioService, times(1)).buscarUsuario(1L);
	}
	
	@Test
	void listarUsuarios() throws Exception {
		
		UsuarioDTO usuario2 = new UsuarioDTO();
		usuario2.setNombre("Julio");
		usuario2.setApellido("De la vega");
		usuario2.setDni("94713376");
		usuario2.setGenero("M");
		usuario2.setEdad(18);
		usuario2.setEmail("julio@gmail.com");
		usuario2.setCelular("123456789");
		
	     UsuarioDTO usuarioExistente = new UsuarioDTO();
	     usuarioExistente.setNombre("Juan");
	     usuarioExistente.setApellido("Pérez");
	     usuarioExistente.setDni("12345678");
	     usuarioExistente.setEdad(30);
	     usuarioExistente.setGenero("M");
	     usuarioExistente.setEmail("juan.perez@gmail.com");
	     usuarioExistente.setCelular("123456789");
	     
	     List<UsuarioDTO> listar = new ArrayList<>();
	     listar.add(usuario2);
	     listar.add(usuarioExistente);
	     
	     when(usuarioService.listarUsuariosPorBanco()).thenReturn(listar);
	     
	     this.mockMvc.perform(get("/api/listarUsuarios"))
	     			.andExpect(status().isOk())
	     			.andExpect(jsonPath("$.size()", is(listar.size())));
	}
	
	@Test
	void actualizarUsuario() throws Exception {
		
			UsuarioDTO usuarioOriginal = new UsuarioDTO();
			usuarioOriginal.setId(1L);
		    usuarioOriginal.setNombre("Juan");
		    usuarioOriginal.setApellido("Perez");
		    usuarioOriginal.setDni("12345678");
		    usuarioOriginal.setEdad(30);
		    usuarioOriginal.setGenero("M");
		    usuarioOriginal.setEmail("juanperez@gmail.com");
		    usuarioOriginal.setCelular("123456789");

		    UsuarioDTO usuarioActualizado = new UsuarioDTO();
		    usuarioActualizado.setId(1L);
		    usuarioActualizado.setNombre("Juan");
		    usuarioActualizado.setApellido("Perez Gomez");
		    usuarioActualizado.setDni("12345679");
		    usuarioActualizado.setEdad(30);
		    usuarioActualizado.setGenero("M");
		    usuarioActualizado.setEmail("juanperez2@gmail.com");
		    usuarioActualizado.setCelular("123456788");
		
	        when(usuarioService.actualizarUsuario(anyLong(),any(UsuarioDTO.class))).thenReturn(usuarioActualizado);
	        


	        mockMvc.perform(put("/api/actualizarUsuario/{usuarioId}",1L)
	        		.contentType(MediaType.APPLICATION_JSON)
	        		.content(objectMapper.writeValueAsString(usuarioActualizado)))
	                .andExpect(status().isOk());
	             
	        verify(usuarioService, times(1)).actualizarUsuario(1L, usuarioActualizado);

	}
	
	@Test
	void eliminarUsuario() throws Exception {
		
		UsuarioDTO usuarioOriginal = new UsuarioDTO();
	    usuarioOriginal.setNombre("Juan");
	    usuarioOriginal.setApellido("Perez");
	    usuarioOriginal.setDni("12345678");
	    usuarioOriginal.setEdad(30);
	    usuarioOriginal.setGenero("M");
	    usuarioOriginal.setEmail("juanperez@gmail.com");
	    usuarioOriginal.setCelular("123456789");
	    
	    doNothing().when(usuarioService).eliminarUsuario(anyLong());
	    
	    this.mockMvc.perform(delete("/api/eliminarUsuario/{usuarioId}",1L))
	    .andExpect(status().isNoContent());
		
	}
}
