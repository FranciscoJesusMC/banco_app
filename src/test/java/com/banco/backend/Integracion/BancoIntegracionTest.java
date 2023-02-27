package com.banco.backend.Integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import com.banco.backend.dto.BancoDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BancoIntegracionTest {

	private String baseurl = "http://localhost:8081/api";
	
	private static RestTemplate restTemplate;
	
	@BeforeEach
	public void setUp() {
	        restTemplate = new RestTemplate();
	 }
	
	
	@Test
	void crearBanco() {
		
		BancoDTO bancoDTO = new BancoDTO();
		bancoDTO.setNombre("Banco prueba");
		
		ResponseEntity<BancoDTO> response = restTemplate.postForEntity(baseurl+"/banco/crearBanco", bancoDTO, BancoDTO.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(bancoDTO.getNombre(), response.getBody().getNombre());
	}
	
	@Test
	void listarBancos() {
		
		BancoDTO[] bancos = restTemplate.getForObject(baseurl+"/banco", BancoDTO[].class);
		
		assertEquals(2, bancos.length);
	}
	
	@Test
	void buscarBancoPorId() {
		 
		ResponseEntity<BancoDTO> banco = restTemplate.getForEntity(baseurl+"/banco/"+2, BancoDTO.class);
		
		assertNotNull(banco);
		assertEquals("Banco prueba", banco.getBody().getNombre());
	}
	
	@Test
	void actualizarBanco() {
		
		Long id = 2L;
		
		BancoDTO bancoActualizado = new BancoDTO();
		bancoActualizado.setNombre("Banco actualizado");
		
		restTemplate.put(baseurl+"/banco/"+id, bancoActualizado);
		
		ResponseEntity<BancoDTO> buscarBanco = restTemplate.getForEntity(baseurl+"/banco/"+id, BancoDTO.class);
		
		assertEquals(HttpStatus.OK, buscarBanco.getStatusCode());
		assertNotNull(buscarBanco.getBody());
		
		assertEquals(bancoActualizado.getNombre(), buscarBanco.getBody().getNombre());
		
	}
	
	@Test
	void eliminarBanco() {
		
		BancoDTO bancoParaBorrar = new BancoDTO();
		bancoParaBorrar.setNombre("BancoBorrado");
		
		ResponseEntity<BancoDTO> guardarBanco = restTemplate.postForEntity(baseurl+"/banco/crearBanco", bancoParaBorrar, BancoDTO.class);
				
		Long bancoId = guardarBanco.getBody().getId();
		
		ResponseEntity<BancoDTO> banco = restTemplate.getForEntity(baseurl+"/banco/"+bancoId, BancoDTO.class);
		assertNotNull(banco);
		
		restTemplate.delete(baseurl+"/banco/"+bancoId);
		
		BancoDTO[] bancos = restTemplate.getForObject(baseurl+"/banco", BancoDTO[].class);
		
		assertEquals(3, bancos.length);
		
	
		
	}
}
