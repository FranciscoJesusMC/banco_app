package com.banco.backend.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.banco.backend.dto.BancoDTO;
import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.dto.MovimientoDTO;
import com.banco.backend.dto.TipoCuentaDTO;
import com.banco.backend.dto.TipoTransaccionDTO;
import com.banco.backend.dto.UsuarioDTO;
import com.banco.backend.entity.DetalleMovimiento;
import com.banco.backend.entity.Movimiento;
import com.banco.backend.serviceImpl.MovimientoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MovimientoController.class)
@Import({MovimientoController.class,MovimientoServiceImpl.class})
public class MovimientoControllerTest {
	
	@MockBean
	private MovimientoServiceImpl movimientosService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	
	@Test
	void realizarDeposito() throws Exception{
		
        long bancoId = 1L;
        UUID cuentaId = UUID.randomUUID();
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setMonto(new BigDecimal("100.00"));

        // Definir comportamiento del servicio
        when(movimientosService.agregarSaldo(bancoId, cuentaId, movimientoDTO)).thenReturn(movimientoDTO);

        // Crear petición HTTP
        String url = String.format("/api/movimientos/agregarSaldo/banco/%d/cuenta/%s", bancoId, cuentaId.toString());
        String jsonRequest = objectMapper.writeValueAsString(movimientoDTO);

        mockMvc.perform(
                put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
        .andDo(print())
        .andExpect(status().isOk());
        
    }
	
	@Test
	void realizarRetiro() throws Exception{
		
        long bancoId = 1L;
        UUID cuentaId = UUID.randomUUID();
        long usuarioId =1L;
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setMonto(new BigDecimal("100.00"));

        // Definir comportamiento del servicio
        when(movimientosService.agregarSaldo(bancoId, cuentaId, movimientoDTO)).thenReturn(movimientoDTO);

        // Crear petición HTTP
        String url = String.format("/api/movimientos/retiro/banco/%d/usuario/%d/cuenta/%s", bancoId, usuarioId,cuentaId.toString());
        String jsonRequest = objectMapper.writeValueAsString(movimientoDTO);

        mockMvc.perform(
                put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
        .andDo(print())
        .andExpect(status().isOk());
		
	}
	
}
