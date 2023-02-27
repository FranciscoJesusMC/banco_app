package com.banco.backend.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.banco.backend.dto.BancoDTO;
import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.dto.TipoCuentaDTO;
import com.banco.backend.dto.UsuarioDTO;
import com.banco.backend.serviceImpl.CuentaServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CuentaController.class)
@Import({CuentaController.class, CuentaServiceImpl.class})
public class CuentaControllerTest {

	
	@MockBean
	private CuentaServiceImpl cuentaSevice;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void crearCuenta() throws Exception {
		
		TipoCuentaDTO tipoCuenta = new TipoCuentaDTO();
		tipoCuenta.setId(1L);
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");


		BancoDTO banco = new BancoDTO();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
		
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero("M");
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");

		CuentaDTO cuenta = new CuentaDTO();
		cuenta.setBanco(banco);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setUsuario(usuario);

		
		when(cuentaSevice.crearCuenta(anyLong(),anyLong(),anyLong())).thenReturn(cuenta);
		
		this.mockMvc.perform(post("/api/crearCuenta/banco/{bancoId}/usuario/{usuarioId}/tipoCuenta/{tipoCuentaId}",1L,1L,1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cuenta)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.banco.nombre", is(cuenta.getBanco().getNombre())));
		
		
		verify(cuentaSevice,times(1)).crearCuenta(anyLong(), anyLong(), anyLong());
	}
	
	@Test
	void listarCuentas() throws Exception {
		
		TipoCuentaDTO tipoCuenta = new TipoCuentaDTO();
		tipoCuenta.setId(1L);
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");
		
		TipoCuentaDTO tipoCuenta2 = new TipoCuentaDTO();
		tipoCuenta2.setId(2L);
		tipoCuenta2.setNombre("Cuenta de prueba2");
		tipoCuenta2.setDescripcion("hola2");
		
		
		BancoDTO banco = new BancoDTO();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
		
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero("M");
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");

		CuentaDTO cuenta = new CuentaDTO();
		cuenta.setBanco(banco);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setUsuario(usuario);
		
		CuentaDTO cuenta2 = new CuentaDTO();
		cuenta2.setBanco(banco);
		cuenta2.setTipoCuenta(tipoCuenta2);
		cuenta2.setUsuario(usuario);
		
		List<CuentaDTO> cuentas = new ArrayList<>();
		cuentas.add(cuenta);
		cuentas.add(cuenta2);
		
		when(cuentaSevice.listarCuentasPorUsuarioId(anyLong(), anyLong())).thenReturn(cuentas);
		
		this.mockMvc.perform(get("/api/listarCuentas/banco/{bancoId}/usuario/{usuarioId}",1L,1L))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.size()", is(cuentas.size())));
		
	}
	
	@Test
	void deshabilitarCuentaPorUsuario() throws Exception {
		
		TipoCuentaDTO tipoCuenta = new TipoCuentaDTO();
		tipoCuenta.setId(1L);
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");


		BancoDTO banco = new BancoDTO();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
		
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero("M");
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");

		CuentaDTO cuenta = new CuentaDTO();
		cuenta.setId(UUID.randomUUID());
		cuenta.setBanco(banco);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setUsuario(usuario);
		cuenta.setEstado("Habilitado");
		

		this.mockMvc.perform(put("/api/deshabilitarCuenta/banco/{bancoId}/usuario/{usuarioId}/cuenta/{cuentaId}",1L,1L,cuenta.getId()))
		.andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Cuenta deshabilitada con exito"));
	    
		
	}
}
