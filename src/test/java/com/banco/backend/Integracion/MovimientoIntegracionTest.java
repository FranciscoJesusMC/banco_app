package com.banco.backend.Integracion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.banco.backend.dto.BancoDTO;
import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.dto.MovimientoDTO;
import com.banco.backend.dto.TipoCuentaDTO;
import com.banco.backend.dto.TipoTransaccionDTO;
import com.banco.backend.dto.TransferenciaDTO;
import com.banco.backend.dto.TransferenciaInterbancariaDTO;
import com.banco.backend.dto.UsuarioDTO;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovimientoIntegracionTest {

	private String baseurl = "http://localhost:8081/api";
	
	private static RestTemplate restTemplate;
	
	private TipoCuentaDTO tipoCuenta;
			
	private BancoDTO banco;
	
	private BancoDTO banco2;
	
	private UsuarioDTO usuario;
	
	private UsuarioDTO usuario2;
	
	private CuentaDTO cuenta;
	
	private TipoTransaccionDTO tipoTransaccionDeposito;
		
	private TipoTransaccionDTO tipoTransaccionRetiro;
	
	private TipoTransaccionDTO tipoTransaccionBancaria;
	
	private TipoTransaccionDTO tipoTransaccionInterbancaria;
		
	@BeforeEach
	public void setUp() throws Exception {
		
	        restTemplate = new RestTemplate();
	       
	        //Crear tipo de cuenta
	        TipoCuentaDTO newTipoCuenta = new TipoCuentaDTO();
	        newTipoCuenta.setNombre("Cuenta de prueba");
	        newTipoCuenta.setDescripcion("hola");
						
			HttpHeaders headersTipoCuenta = new HttpHeaders();
			headersTipoCuenta.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<TipoCuentaDTO> requestTipoCuenta = new HttpEntity<>(newTipoCuenta,headersTipoCuenta);
			
			ResponseEntity<TipoCuentaDTO> responseTipo = restTemplate.postForEntity(baseurl+"/tipoCuenta", requestTipoCuenta, TipoCuentaDTO.class);
			if(responseTipo.getStatusCode() == HttpStatus.CREATED) {
				tipoCuenta = responseTipo.getBody();
			}
			
			
			//Crear bancos
			//Banco1
			BancoDTO newbanco = new BancoDTO();
			newbanco.setNombre("Banco nuevo");
			
			HttpHeaders headersBanco = new HttpHeaders();
			headersBanco.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<BancoDTO> requestBanco = new HttpEntity<>(newbanco,headersBanco);
			
			ResponseEntity<BancoDTO> responseBanco = restTemplate.postForEntity(baseurl+"/banco/crearBanco", requestBanco, BancoDTO.class);
			if(responseBanco.getStatusCode() == HttpStatus.CREATED) {
				banco = responseBanco.getBody();
			}
			
			//Banco2
			BancoDTO newbanco2 = new BancoDTO();
			newbanco2.setNombre("Banco numero2");
						
			HttpHeaders headersBanco2 = new HttpHeaders();
			headersBanco2.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<BancoDTO> requestBanco2 = new HttpEntity<>(newbanco2,headersBanco2);
			
			ResponseEntity<BancoDTO> responseBanco2 = restTemplate.postForEntity(baseurl+"/banco/crearBanco", requestBanco2, BancoDTO.class);
			if(responseBanco2.getStatusCode() == HttpStatus.CREATED) {
				banco2 = responseBanco2.getBody();
			}
			
			
			//Usuarios
			//Usuario1
			UsuarioDTO newusuario = new UsuarioDTO();
			newusuario.setNombre("Francisco");
			newusuario.setApellido("Maculunco");
			newusuario.setCelular("772299112");
			newusuario.setDni("76337190");
			newusuario.setGenero("M");
			newusuario.setEdad(18);
			newusuario.setEmail("franck1@gmail.com");
			
			HttpHeaders headersUsuario = new HttpHeaders();
			headersUsuario.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<UsuarioDTO> requestUsuario = new HttpEntity<>(newusuario,headersUsuario);
			
			ResponseEntity<UsuarioDTO> responseUsuario = restTemplate.postForEntity(baseurl+"/crearUsuario", requestUsuario, UsuarioDTO.class);
			if(responseUsuario.getStatusCode() == HttpStatus.CREATED) {
				usuario = responseUsuario.getBody();
			}
			
			//Usuario2
			UsuarioDTO newusuario2 = new UsuarioDTO();
			newusuario2.setNombre("Ricardo");
			newusuario2.setApellido("Rojas");
			newusuario2.setCelular("123456789");
			newusuario2.setDni("12345678");
			newusuario2.setGenero("M");
			newusuario2.setEdad(18);
			newusuario2.setEmail("ricardo@gmail.com");
			
			HttpHeaders headersUsuario2 = new HttpHeaders();
			headersUsuario2.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<UsuarioDTO> requestUsuario2 = new HttpEntity<>(newusuario2,headersUsuario2);
			
			ResponseEntity<UsuarioDTO> responseUsuario2 = restTemplate.postForEntity(baseurl+"/crearUsuario", requestUsuario2, UsuarioDTO.class);
			if(responseUsuario2.getStatusCode() == HttpStatus.CREATED) {
				usuario2 = responseUsuario2.getBody();
			}
			
			
			
			//Cuenta Origen
			CuentaDTO newcuenta = new CuentaDTO();
			newcuenta.setBanco(banco);
			newcuenta.setTipoCuenta(tipoCuenta);
			newcuenta.setUsuario(usuario);
		
			ResponseEntity<CuentaDTO> responseCuenta = restTemplate.postForEntity(baseurl+"/crearCuenta/banco/"+banco.getId()  +"/usuario/"+ usuario.getId() +"/tipoCuenta/"+tipoCuenta.getId() , null, CuentaDTO.class);
			if(responseCuenta.getStatusCode() == HttpStatus.CREATED) {
				cuenta = responseCuenta.getBody();
			}
			
			
			//Tipos de transacciones
			//Deposito
			TipoTransaccionDTO newTipoTransaccion = new TipoTransaccionDTO();
			newTipoTransaccion.setNombre("Deposito");
			
			HttpHeaders headersTipoDeposito = new HttpHeaders();
			headersTipoDeposito.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<TipoTransaccionDTO> requestTipoDeposito = new HttpEntity<>(newTipoTransaccion,headersTipoDeposito);
			
			ResponseEntity<TipoTransaccionDTO> responseTipoDeposito = restTemplate.postForEntity(baseurl+"/tipoTransaccion", requestTipoDeposito, TipoTransaccionDTO.class);
			if(responseTipoDeposito.getStatusCode() == HttpStatus.CREATED) {
				tipoTransaccionDeposito = responseTipoDeposito.getBody();
			}
			
			//Retiro
			TipoTransaccionDTO newTipoTransaccion2 = new TipoTransaccionDTO();
			newTipoTransaccion2.setNombre("Retiro");
			
			HttpHeaders headersTipoRetiro = new HttpHeaders();
			headersTipoRetiro.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<TipoTransaccionDTO> requestTipoRetiro = new HttpEntity<>(newTipoTransaccion2,headersTipoRetiro);
			
			ResponseEntity<TipoTransaccionDTO> responseTipoRetiro = restTemplate.postForEntity(baseurl+"/tipoTransaccion", requestTipoRetiro, TipoTransaccionDTO.class);
			if(responseTipoRetiro.getStatusCode() == HttpStatus.CREATED) {
				tipoTransaccionRetiro = responseTipoRetiro.getBody();
			}
			
			//Bancaria
			TipoTransaccionDTO  newTipoTransaccion3 = new TipoTransaccionDTO();
			newTipoTransaccion3.setNombre("Transferencia_bancaria");
			
			HttpHeaders headersTipoBancaria = new HttpHeaders();
			headersTipoBancaria.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<TipoTransaccionDTO> requestTipoBancaria = new HttpEntity<>(newTipoTransaccion3,headersTipoBancaria);
			
			ResponseEntity<TipoTransaccionDTO> responseTipoBancaria = restTemplate.postForEntity(baseurl+"/tipoTransaccion", requestTipoBancaria, TipoTransaccionDTO.class);
			if(responseTipoBancaria.getStatusCode() == HttpStatus.CREATED) {
				tipoTransaccionBancaria = responseTipoBancaria.getBody();
			}
			
			
			//Interbancaria
			TipoTransaccionDTO newTipoTransaccion4= new TipoTransaccionDTO();
			newTipoTransaccion4.setNombre("Transferencia_interbancaria");
			
			HttpHeaders headersTipoInterbancaria = new HttpHeaders();
			headersTipoInterbancaria.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<TipoTransaccionDTO> requestTipoInterbancaria = new HttpEntity<>(newTipoTransaccion4,headersTipoInterbancaria);
			
			ResponseEntity<TipoTransaccionDTO> responseTipoInterbancaria = restTemplate.postForEntity(baseurl+"/tipoTransaccion", requestTipoInterbancaria, TipoTransaccionDTO.class);
			if(responseTipoInterbancaria.getStatusCode() == HttpStatus.CREATED) {
				tipoTransaccionInterbancaria = responseTipoInterbancaria.getBody();
			}
			
	 }
	
	@Test
	void realizarDeposito() {
				
		//Crear movimiento y cabecera
		MovimientoDTO movimiento = new MovimientoDTO();
	    movimiento.setMonto(new BigDecimal("100.00"));

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<MovimientoDTO> requestEntity = new HttpEntity<>(movimiento, headers);
	    
	    
	    //Realizando operacion
	    ResponseEntity<MovimientoDTO> response = restTemplate.exchange(baseurl+"/movimientos/agregarSaldo/banco/"+banco.getId()+"/cuenta/"+ cuenta.getId(),HttpMethod.PUT,requestEntity,MovimientoDTO.class);
		
	    assertNotNull(response.getBody());
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(response.getBody().getTipoTransaccion().getNombre(), tipoTransaccionDeposito.getNombre());
	    assertEquals(new BigDecimal("100.00"), response.getBody().getMonto());
	        
	    //Verificando que a la cuenta se le agrego el saldo
	    ResponseEntity<CuentaDTO> buscarCuenta = restTemplate.getForEntity(baseurl+"/buscarCuenta/banco/"+banco.getId()+"/cuenta/"+cuenta.getId(), CuentaDTO.class);
	      
	    assertNotNull(buscarCuenta);
	    assertEquals(HttpStatus.OK, buscarCuenta.getStatusCode());
	    assertEquals(new BigDecimal("100.00"), buscarCuenta.getBody().getSaldo());
	    assertEquals(1, buscarCuenta.getBody().getDepositosDelDia());
	       
	}
	
	@Test
	void realizarRetiro() {	
		
		//Agregando saldo a la cuenta
		MovimientoDTO movimientoSaldo = new MovimientoDTO();
		movimientoSaldo.setMonto(new BigDecimal("300.00"));
		
		HttpHeaders headersSaldo = new HttpHeaders();
		headersSaldo.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MovimientoDTO> requestEntitySaldo = new HttpEntity<>(movimientoSaldo,headersSaldo);
		
		ResponseEntity<MovimientoDTO> agregarSaldo = restTemplate.exchange(baseurl+"/movimientos/agregarSaldo/banco/"+banco.getId()+"/cuenta/"+ cuenta.getId(),HttpMethod.PUT,requestEntitySaldo,MovimientoDTO.class);
			
		assertNotNull(agregarSaldo);
		assertEquals(movimientoSaldo.getMonto(), agregarSaldo.getBody().getMonto());
		
		//Crear movimiento y la cabecera
		MovimientoDTO movimiento = new MovimientoDTO();
		movimiento.setMonto(new BigDecimal("100.00"));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MovimientoDTO> requestEntity = new HttpEntity<>(movimiento,headers);
		
		
		try {
			
			//verificando operacion
			ResponseEntity<MovimientoDTO> response = restTemplate.exchange(baseurl+"/movimientos/retiro/banco/"+banco.getId()+"/usuario/"+usuario.getId()+"/cuenta/"+cuenta.getId(), HttpMethod.PUT,requestEntity,MovimientoDTO.class);
			
			assertNotNull(response.getBody());
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(tipoTransaccionRetiro.getNombre(), response.getBody().getTipoTransaccion().getNombre());
			assertEquals(new BigDecimal("100.00"), response.getBody().getMonto());
			
			
			//Verificando cuenta
			 ResponseEntity<CuentaDTO> buscarCuenta = restTemplate.getForEntity(baseurl+"/buscarCuenta/banco/"+banco.getId()+"/cuenta/"+cuenta.getId(), CuentaDTO.class);
			 
			 assertNotNull(buscarCuenta.getBody());
			 assertEquals(new BigDecimal("200.00"), buscarCuenta.getBody().getSaldo());
			 assertEquals(1, buscarCuenta.getBody().getRetirosDelDia());
			 assertEquals(new BigDecimal("400.00"), buscarCuenta.getBody().getLimiteDelDia());
			
			
		} catch (HttpClientErrorException.BadRequest ex){
			
		    String mensajeEsperado = "La cuenta no posee el saldo suficiente";
		    String errorMensaje = ex.getResponseBodyAsString();

		    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		    assertThat(errorMensaje).contains(mensajeEsperado);
		}
		
	}
	
	@Test
	void realizarTransferenciaBancaria() {
			
		//Agregando saldo a la cuenta origen
		MovimientoDTO movimiento = new MovimientoDTO();
		movimiento.setMonto(new BigDecimal("200.00"));
		
		HttpHeaders headers1 = new HttpHeaders();
		headers1.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MovimientoDTO> requestEntity1 = new HttpEntity<>(movimiento,headers1);
		
		ResponseEntity<MovimientoDTO> agregarSaldo = restTemplate.exchange(baseurl+"/movimientos/agregarSaldo/banco/"+banco.getId()+"/cuenta/"+ cuenta.getId(), HttpMethod.PUT,requestEntity1,MovimientoDTO.class);
		
		assertNotNull(agregarSaldo);
		assertEquals(movimiento.getMonto(), agregarSaldo.getBody().getMonto());
		
	
		//Creando cuenta 2		
		ResponseEntity<CuentaDTO> responseCuenta2 = restTemplate.postForEntity(baseurl+"/crearCuenta/banco/"+banco.getId()  +"/usuario/"+ usuario2.getId() +"/tipoCuenta/"+ tipoCuenta.getId(), null, CuentaDTO.class);
		UUID cuentaId2 = responseCuenta2.getBody().getId();
		
		//Encabezado de la transferencia
		TransferenciaDTO transferencia = new TransferenciaDTO();
		transferencia.setCuentaDestino(cuentaId2);
		transferencia.setMonto(new BigDecimal("100.00"));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TransferenciaDTO> requestEntity = new HttpEntity<>(transferencia,headers);
		
		
		
		try {
			
			//Verificando operacion
			ResponseEntity<MovimientoDTO> response = restTemplate.exchange(baseurl+"/movimientos/transferenciaBancaria/banco/"+banco.getId()+"/usuario/"+usuario.getId()+"/cuenta/"+cuenta.getId(), HttpMethod.PUT, requestEntity,MovimientoDTO.class);
			
			assertNotNull(response.getBody());
			assertEquals(tipoTransaccionBancaria.getNombre(), response.getBody().getTipoTransaccion().getNombre());
			assertEquals(new BigDecimal("100.00"), response.getBody().getMonto());
			
			//Verificando cuenta
			ResponseEntity<CuentaDTO> buscarCuenta2 = restTemplate.getForEntity(baseurl+"/buscarCuenta/banco/"+banco.getId()+"/cuenta/"+cuentaId2, CuentaDTO.class);
		      
			assertNotNull(response);
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(new BigDecimal("100.00"), buscarCuenta2.getBody().getSaldo());
			
		} catch (HttpClientErrorException.BadRequest ex){
			
		    String mensajeEsperado = "La cuenta Origen no posee el saldo suficiente";
		    String errorMensaje = ex.getResponseBodyAsString();

		    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		    assertThat(errorMensaje).contains(mensajeEsperado);
		}
		
	}
	
	@Test 
	void crearTransferenciaInterbancaria() {
			
		//CrearCuentaDestino	
		ResponseEntity<CuentaDTO> responseCuenta2 = restTemplate.postForEntity(baseurl+"/crearCuenta/banco/"+banco2.getId()  +"/usuario/"+ usuario2.getId() +"/tipoCuenta/"+ tipoCuenta.getId(), null, CuentaDTO.class);
		Long cci = responseCuenta2.getBody().getCci();
		UUID cuentaId2 = responseCuenta2.getBody().getId();
		
		
		//Agregando saldo a la cuenta origen
		MovimientoDTO movimiento = new MovimientoDTO();
		movimiento.setMonto(new BigDecimal("200.00"));
		
		HttpHeaders headers1 = new HttpHeaders();
		headers1.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MovimientoDTO> requestEntity1 = new HttpEntity<>(movimiento,headers1);
		
		ResponseEntity<MovimientoDTO> agregarSaldo = restTemplate.exchange(baseurl+"/movimientos/agregarSaldo/banco/"+banco.getId()+"/cuenta/"+ cuenta.getId(), HttpMethod.PUT,requestEntity1,MovimientoDTO.class);
		
		assertNotNull(agregarSaldo);
		assertEquals(movimiento.getMonto(), agregarSaldo.getBody().getMonto());
		
		
		//Encabezado de la transferenciaInterbancaria
		TransferenciaInterbancariaDTO transferencia = new TransferenciaInterbancariaDTO();
		transferencia.setCuentaDestino(cci);
		transferencia.setMonto(new BigDecimal("100.00"));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TransferenciaInterbancariaDTO> requestEntity = new HttpEntity<>(transferencia,headers);
		
		

		try {
			
			//Transferencia
			ResponseEntity<MovimientoDTO> response = restTemplate.exchange(baseurl+"/movimientos/transferenciaInterbancaria/banco/"+banco.getId()+"/usuario/"+usuario.getId()+"/cuenta/"+cuenta.getId(), HttpMethod.PUT, requestEntity,MovimientoDTO.class);
			
			assertNotNull(response.getBody());
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(tipoTransaccionInterbancaria.getNombre(),response.getBody().getTipoTransaccion().getNombre());
			
			//Cuenta1
			ResponseEntity<CuentaDTO> buscarCuenta1 = restTemplate.getForEntity(baseurl+"/buscarCuenta/banco/"+banco.getId()+"/cuenta/"+cuenta.getId(), CuentaDTO.class);
			   
			assertNotNull(buscarCuenta1.getBody());
			assertEquals(new BigDecimal("100.00"), buscarCuenta1.getBody().getSaldo());
			
			//Cuenta2
			ResponseEntity<CuentaDTO> buscarCuenta2 = restTemplate.getForEntity(baseurl+"/buscarCuenta/banco/"+banco2.getId()+"/cuenta/"+cuentaId2, CuentaDTO.class);
		    
			assertNotNull(buscarCuenta2);
			assertEquals(new BigDecimal("95.00"), buscarCuenta2.getBody().getSaldo());
			
			
		}  catch (HttpClientErrorException.BadRequest ex){
			
		    String mensajeEsperado = "La cuenta Origen no posee el saldo suficiente";
		    String errorMensaje = ex.getResponseBodyAsString();

		    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		    assertThat(errorMensaje).contains(mensajeEsperado);
		}
		
		
	}
}
