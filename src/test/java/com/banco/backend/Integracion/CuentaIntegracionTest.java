package com.banco.backend.Integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import com.banco.backend.dto.BancoDTO;
import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.dto.TipoCuentaDTO;
import com.banco.backend.dto.UsuarioDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CuentaIntegracionTest {
	
	private String baseUrl = "http://localhost:8081/api";
	
	private static RestTemplate restTemplate;
	
	private TipoCuentaDTO tipoCuenta;
	
	private TipoCuentaDTO tipoCuenta2;
	
	private BancoDTO banco;
	
	private UsuarioDTO usuario;
		
	@BeforeEach
	void beforeInit() {

		restTemplate = new RestTemplate();
	
		tipoCuenta = new TipoCuentaDTO();
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");
		
		tipoCuenta2 = new TipoCuentaDTO();
		tipoCuenta2.setNombre("Cuenta de prueba2");
		tipoCuenta2.setDescripcion("hola2");
		
		
		banco = new BancoDTO();
		banco.setNombre("Banco nuevo");
		
		
		usuario = new UsuarioDTO();
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("772299112");
		usuario.setDni("76337190");
		usuario.setGenero("M");
		usuario.setEdad(18);
		usuario.setEmail("franck1@gmail.com");
			
	}
		
	@Test
	void crearCuenta() {
				
		ResponseEntity<TipoCuentaDTO> responseTipo = restTemplate.postForEntity(baseUrl+"/tipoCuenta", tipoCuenta, TipoCuentaDTO.class);
		Long tipoId = responseTipo.getBody().getId();
		
		ResponseEntity<BancoDTO> responseBanco = restTemplate.postForEntity(baseUrl+"/banco/crearBanco", banco, BancoDTO.class);
		Long bancoId = responseBanco.getBody().getId();
		
		ResponseEntity<UsuarioDTO> responseUsuario = restTemplate.postForEntity(baseUrl+"/crearUsuario", usuario, UsuarioDTO.class);
		Long usuarioId = responseUsuario.getBody().getId();
		
		ResponseEntity<CuentaDTO> response = restTemplate.postForEntity(baseUrl+"/crearCuenta/banco/"+bancoId  +"/usuario/"+ usuarioId +"/tipoCuenta/"+ tipoId, null, CuentaDTO.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(bancoId, response.getBody().getBanco().getId());
		assertEquals(tipoId, response.getBody().getTipoCuenta().getId());
	}

	@Test 
	void obtenerCuentaPorId(){
		
		ResponseEntity<TipoCuentaDTO> responseTipo = restTemplate.postForEntity(baseUrl+"/tipoCuenta", tipoCuenta, TipoCuentaDTO.class);
		Long tipoId = responseTipo.getBody().getId();
		
		ResponseEntity<BancoDTO> responseBanco = restTemplate.postForEntity(baseUrl+"/banco/crearBanco", banco, BancoDTO.class);
		Long bancoId = responseBanco.getBody().getId();
		
		ResponseEntity<UsuarioDTO> responseUsuario = restTemplate.postForEntity(baseUrl+"/crearUsuario", usuario, UsuarioDTO.class);
		Long usuarioId = responseUsuario.getBody().getId();
		
		ResponseEntity<CuentaDTO> responseCuenta = restTemplate.postForEntity(baseUrl+"/crearCuenta/banco/"+bancoId  +"/usuario/"+ usuarioId +"/tipoCuenta/"+ tipoId, null, CuentaDTO.class);
		UUID cuentaId = responseCuenta.getBody().getId();
		
		
		ResponseEntity<CuentaDTO> response = restTemplate.getForEntity(baseUrl+"/buscarCuenta/banco/"+bancoId+"/cuenta/"+cuentaId , CuentaDTO.class);
	
		assertNotNull(response);
		assertEquals(bancoId, response.getBody().getBanco().getId());
		assertEquals("Deshabilitada", response.getBody().getEstado());
	}
	
	@Test
	void listarCuentasDelusuario() {
		
		//Tipos de cuenta
		ResponseEntity<TipoCuentaDTO> responseTipo = restTemplate.postForEntity(baseUrl+"/tipoCuenta", tipoCuenta, TipoCuentaDTO.class);
		Long tipoId = responseTipo.getBody().getId();
		
		ResponseEntity<TipoCuentaDTO> responseTipo2 = restTemplate.postForEntity(baseUrl+"/tipoCuenta", tipoCuenta2, TipoCuentaDTO.class);
		Long tipoId2 = responseTipo2.getBody().getId();
		
		//Banco
		ResponseEntity<BancoDTO> responseBanco = restTemplate.postForEntity(baseUrl+"/banco/crearBanco", banco, BancoDTO.class);
		Long bancoId = responseBanco.getBody().getId();
		
		//Usuario
		ResponseEntity<UsuarioDTO> responseUsuario = restTemplate.postForEntity(baseUrl+"/crearUsuario", usuario, UsuarioDTO.class);
		Long usuarioId = responseUsuario.getBody().getId();
		
		//Cuentas
		ResponseEntity<CuentaDTO> responseCuenta = restTemplate.postForEntity(baseUrl+"/crearCuenta/banco/"+bancoId  +"/usuario/"+ usuarioId +"/tipoCuenta/"+ tipoId, null, CuentaDTO.class);
		UUID cuentaId = responseCuenta.getBody().getId();
		
		ResponseEntity<CuentaDTO> responseCuenta2 = restTemplate.postForEntity(baseUrl+"/crearCuenta/banco/"+bancoId  +"/usuario/"+ usuarioId +"/tipoCuenta/"+ tipoId2, null, CuentaDTO.class);
		UUID cuentaId2 = responseCuenta2.getBody().getId();
		
		//Recueperamos las cuentas del usuario
		CuentaDTO[] lista  = restTemplate.getForObject(baseUrl+"/listarCuentas/banco/"+bancoId +"/usuario/"+usuarioId, CuentaDTO[].class);
		
		
		assertEquals(2, lista.length);
		
		CuentaDTO cuentaBuscada= null;
		CuentaDTO cuentaBuscada2= null;
		
		for(CuentaDTO cuenta :lista) {
			if(cuenta.getId().equals(cuentaId)) {
				cuentaBuscada = cuenta;
			}
			if(cuenta.getId().equals(cuentaId2)) {
				cuentaBuscada2 = cuenta;
			}
			if(cuentaBuscada != null && cuentaBuscada2 != null) {
				break;
			}
		}
		
		assertNotNull(cuentaBuscada);
		assertNotNull(cuentaBuscada2);
		
	}
	
	@Test
	void deshabilitarCuenta() {
		
		ResponseEntity<TipoCuentaDTO> responseTipo = restTemplate.postForEntity(baseUrl+"/tipoCuenta", tipoCuenta, TipoCuentaDTO.class);
		Long tipoId = responseTipo.getBody().getId();
		
		ResponseEntity<BancoDTO> responseBanco = restTemplate.postForEntity(baseUrl+"/banco/crearBanco", banco, BancoDTO.class);
		Long bancoId = responseBanco.getBody().getId();
		
		ResponseEntity<UsuarioDTO> responseUsuario = restTemplate.postForEntity(baseUrl+"/crearUsuario", usuario, UsuarioDTO.class);
		Long usuarioId = responseUsuario.getBody().getId();
		
		ResponseEntity<CuentaDTO> responseCuenta = restTemplate.postForEntity(baseUrl+"/crearCuenta/banco/"+bancoId  +"/usuario/"+ usuarioId +"/tipoCuenta/"+ tipoId, null, CuentaDTO.class);
		UUID cuentaId = responseCuenta.getBody().getId();
		
		
		restTemplate.put( baseUrl + "/deshabilitarCuenta/banco/"+bancoId +"/usuario/"+usuarioId+"/cuenta/"+ cuentaId,null);
	
		ResponseEntity<CuentaDTO> response = restTemplate.getForEntity(baseUrl+"/buscarCuenta/banco/"+bancoId+"/cuenta/"+cuentaId , CuentaDTO.class);
		
		assertNotNull(response);
		assertEquals("Deshabilitada", response.getBody().getEstado());
	}
}
