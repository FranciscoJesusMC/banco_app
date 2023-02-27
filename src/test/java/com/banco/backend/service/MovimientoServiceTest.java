package com.banco.backend.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.banco.backend.dto.MovimientoDTO;
import com.banco.backend.dto.TransferenciaDTO;
import com.banco.backend.dto.TransferenciaInterbancariaDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Cuenta;
import com.banco.backend.entity.DetalleMovimiento;
import com.banco.backend.entity.Movimiento;
import com.banco.backend.entity.TipoCuenta;
import com.banco.backend.entity.TipoTransaccion;
import com.banco.backend.entity.Usuario;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.CuentaRepositorio;
import com.banco.backend.repository.DetalleMovimientoRepositorio;
import com.banco.backend.repository.MovimientoRepositorio;
import com.banco.backend.repository.TipoCuentaRepositorio;
import com.banco.backend.repository.TipoTransaccionRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.serviceImpl.MovimientoServiceImpl;
import com.banco.backend.utils.Genero;

@ExtendWith(MockitoExtension.class)
public class MovimientoServiceTest {

	private MovimientoRepositorio movimientoRepositorio = Mockito.mock(MovimientoRepositorio.class);
	
	private CuentaRepositorio cuentaRepositorio = Mockito.mock(CuentaRepositorio.class);

	private BancoRepositorio bancoRepositorio = Mockito.mock(BancoRepositorio.class);
	
	private TipoCuentaRepositorio tipoCuentaRepositorio = Mockito.mock(TipoCuentaRepositorio.class);
	
	private UsuarioRepositorio usuarioRepositorio = Mockito.mock(UsuarioRepositorio.class);
	
	private TipoTransaccionRepositorio tipoTransaccionRepositorio = Mockito.mock(TipoTransaccionRepositorio.class);
	
	private DetalleMovimientoRepositorio detalleRepositorio = Mockito.mock(DetalleMovimientoRepositorio.class);
	
	@InjectMocks
	private MovimientoServiceImpl movimientoService;
	
	private TipoCuenta tipoCuenta;
	
	private Banco banco;
	
	private Usuario usuario;
	
	private Cuenta cuenta;
	
	private TipoTransaccion tipoTransaccion;
	
	private TipoTransaccion tipoTransaccionRetiro;
	
	private TipoTransaccion tipoTransaccionBancaria;
	
	private TipoTransaccion tipoTransaccionInterbancaria;
	
	@BeforeEach
	void init() {
		
		tipoCuenta = new TipoCuenta();
		tipoCuenta.setId(1L);
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");
		
		banco = new Banco();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
		
		usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");

	
		cuenta = new Cuenta();
		cuenta.setId(UUID.randomUUID());
		cuenta.setBanco(banco);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setUsuario(usuario);
		cuenta.setSaldo(new BigDecimal("1000.00"));
		cuenta.setEstado("Habilitada");
		cuenta.setLimiteDelDia(new BigDecimal("500.00"));
		
		tipoTransaccion = new TipoTransaccion();
		tipoTransaccion.setNombre("Deposito");
		
		tipoTransaccionRetiro = new TipoTransaccion();
		tipoTransaccionRetiro.setNombre("Retiro");
		
		tipoTransaccionBancaria = new TipoTransaccion();
		tipoTransaccionBancaria.setNombre("Transferencia_bancaria");
		
		tipoTransaccionInterbancaria = new TipoTransaccion();
		tipoTransaccionInterbancaria.setNombre("Transferencia_interbancaria");
		
	}
	
	@Test
	void crearMovimientoAgregarSaldo() {
		
		Movimiento movimiento = new Movimiento();
		movimiento.setCuenta(cuenta);
		movimiento.setTipoTransaccion(tipoTransaccion);
		movimiento.setMonto(new BigDecimal("100.00"));
		
		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitularDestino(cuenta.getUsuario().getNombre());
		detalle.setTitular(null);
		detalle.setBancoOrigen(null);
		detalle.setCuentaOrigen(null);
		detalle.setCuentaDestino(cuenta.getId());
		detalle.setBancoDestino(banco.getNombre());
		detalle.setComision(new BigDecimal("00.00"));
		detalle.setMontoTotal(movimiento.getMonto());

		MovimientoDTO movimientoDTO = new MovimientoDTO();
		movimientoDTO.setMonto(movimiento.getMonto());
		movimientoDTO.setDetalleMovimiento(detalle);

		when(tipoTransaccionRepositorio.findByNombre("Deposito")).thenReturn(Optional.of(tipoTransaccion));
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		when(cuentaRepositorio.findById(cuenta.getId())).thenReturn(Optional.of(cuenta));
		
		when(movimientoRepositorio.save(any(Movimiento.class))).thenReturn(movimiento);
		
		MovimientoDTO resultado = movimientoService.agregarSaldo(banco.getId(), cuenta.getId(), movimientoDTO);
		
		assertNotNull(resultado);
		assertEquals(new BigDecimal("100.00"), resultado.getMonto());
		assertEquals("Deposito", resultado.getTipoTransaccion().getNombre());
		assertEquals(new BigDecimal("1100.00"), cuenta.getSaldo());
		
	}
	
	@Test
	void crearMovimientoRetiro() {
		
		Movimiento movimiento = new Movimiento();
		movimiento.setCuenta(cuenta);
		movimiento.setTipoTransaccion(tipoTransaccionRetiro);
		movimiento.setMonto(new BigDecimal("100.00"));
		
		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitular(cuenta.getUsuario().getNombre());
		detalle.setCuentaOrigen(cuenta.getId());
		detalle.setCuentaDestino(null);
		detalle.setBancoOrigen(cuenta.getBanco().getNombre());
		detalle.setBancoDestino(null);
		detalle.setTitularDestino(null);
		detalle.setComision(new BigDecimal("00.00"));
		detalle.setMontoTotal(movimiento.getMonto());
		
		MovimientoDTO movimientoDTO = new MovimientoDTO();
		movimientoDTO.setMonto(movimiento.getMonto());
		movimientoDTO.setDetalleMovimiento(detalle);
		
		when(tipoTransaccionRepositorio.findByNombre("Retiro")).thenReturn(Optional.of(tipoTransaccionRetiro));
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.of(usuario));
		when(cuentaRepositorio.findById(cuenta.getId())).thenReturn(Optional.of(cuenta));
		
		when(movimientoRepositorio.save(any(Movimiento.class))).thenReturn(movimiento);
		
		MovimientoDTO resultado = movimientoService.retiro(banco.getId(), usuario.getId(), cuenta.getId(), movimientoDTO);
		
		assertNotNull(resultado);
		assertEquals(new BigDecimal("100.00"), resultado.getMonto());
		assertEquals("Retiro", resultado.getTipoTransaccion().getNombre());
		assertEquals(new BigDecimal("900.00"), cuenta.getSaldo());
		assertEquals(new BigDecimal("400.00"), cuenta.getLimiteDelDia());
	}
	
	@Test
	void crearTransferenciaBancaria() {
		
		Usuario usuario2 = new Usuario();
		usuario2.setId(2L);
		usuario2.setNombre("Ricardo");
		usuario2.setApellido("Rojas");
		usuario2.setCelular("212928255");
		usuario2.setDni("99665522");
		usuario2.setGenero(Genero.M);
		usuario2.setEdad(18);
		usuario2.setEmail("rojas@gmail.com");
		
		Cuenta cuenta2 = new Cuenta();
		cuenta2.setId(UUID.randomUUID());
		cuenta2.setBanco(banco);
		cuenta2.setTipoCuenta(tipoCuenta);
		cuenta2.setUsuario(usuario2);
		cuenta2.setSaldo(new BigDecimal("2000.00"));
		cuenta2.setEstado("Habilitada");
		cuenta2.setLimiteDelDia(new BigDecimal("500.00"));
		
		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitular(cuenta.getUsuario().getNombre());
		detalle.setCuentaOrigen(cuenta.getId());
		detalle.setBancoOrigen(cuenta.getBanco().getNombre());
		detalle.setTitularDestino(cuenta2.getUsuario().getNombre());
		detalle.setCuentaDestino(cuenta2.getId());
		detalle.setBancoDestino(cuenta2.getBanco().getNombre());
		detalle.setComision(new BigDecimal("00.00"));
		
		TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
		transferenciaDTO.setCuentaDestino(cuenta2.getId());
		transferenciaDTO.setTipoTransaccion(tipoTransaccionBancaria);
		transferenciaDTO.setMonto(new BigDecimal("500.00"));
		transferenciaDTO.setDetalleMovimiento(detalle);
		detalle.setMontoTotal(transferenciaDTO.getMonto());
		
		Movimiento movimiento = new Movimiento();
		movimiento.setCuenta(cuenta);
		movimiento.setMonto(new BigDecimal("500.00"));
		movimiento.setTipoTransaccion(tipoTransaccionBancaria);
		
		MovimientoDTO movimientoDTO = new MovimientoDTO();
		movimientoDTO.setMonto(transferenciaDTO.getMonto());
		movimientoDTO.setDetalleMovimiento(detalle);
		
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		when(tipoTransaccionRepositorio.findByNombre("Transferencia_bancaria")).thenReturn(Optional.of(tipoTransaccionBancaria));		
		when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.of(usuario));
		when(cuentaRepositorio.findById(cuenta.getId())).thenReturn(Optional.of(cuenta));
		
		when(cuentaRepositorio.findById(cuenta2.getId())).thenReturn(Optional.of(cuenta2));
		
		
		when(movimientoRepositorio.save(any(Movimiento.class))).thenReturn(movimiento);
		
		MovimientoDTO resultado = movimientoService.transferenciaBancaria(banco.getId(), usuario.getId(), cuenta.getId(), transferenciaDTO);
		
		assertNotNull(resultado);
		assertEquals("Transferencia_bancaria", resultado.getTipoTransaccion().getNombre());
		assertEquals(new BigDecimal("500.00"), cuenta.getSaldo());
		assertEquals(new BigDecimal("2500.00"), cuenta2.getSaldo());
		
		
		
	}
	
	@Test
	void crearTransferenciaInterbancaria() {
		
		Banco banco2 = new Banco();
		banco2.setId(2L);
		banco2.setNombre("Banco interbancario");
		
		Usuario usuario2 = new Usuario();
		usuario2.setId(2L);
		usuario2.setNombre("Ricardo");
		usuario2.setApellido("Rojas");
		usuario2.setCelular("212928255");
		usuario2.setDni("99665522");
		usuario2.setGenero(Genero.M);
		usuario2.setEdad(18);
		usuario2.setEmail("rojas@gmail.com");
		
		Random random = new Random();
		Long crearCci = random.nextLong() % 10000000000L;
		if(crearCci < 0) {
			crearCci = -crearCci;
		}
		
		Cuenta cuenta2 = new Cuenta();
		cuenta2.setId(UUID.randomUUID());
		cuenta2.setCci(crearCci);
		cuenta2.setBanco(banco2);
		cuenta2.setTipoCuenta(tipoCuenta);
		cuenta2.setUsuario(usuario2);
		cuenta2.setSaldo(new BigDecimal("2000.00"));
		cuenta2.setEstado("Habilitada");
		cuenta2.setLimiteDelDia(new BigDecimal("500.00"));
		
		
		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitular(cuenta.getUsuario().getNombre());
		detalle.setCuentaOrigen(cuenta.getId());
		detalle.setBancoOrigen(cuenta.getBanco().getNombre());
		detalle.setTitularDestino(cuenta2.getUsuario().getNombre());
		detalle.setCuentaDestino(cuenta2.getId());
		detalle.setBancoDestino(cuenta2.getBanco().getNombre());
		detalle.setComision(new BigDecimal("5.00"));
		
		
		TransferenciaInterbancariaDTO transferenciaDTO = new TransferenciaInterbancariaDTO();
		transferenciaDTO.setCuentaDestino(cuenta2.getCci());
		transferenciaDTO.setTipoTransaccion(tipoTransaccionInterbancaria);
		transferenciaDTO.setMonto(new BigDecimal("500.00"));
		transferenciaDTO.setDetalleMovimiento(detalle);
		detalle.setMontoTotal(transferenciaDTO.getMonto().subtract(detalle.getComision()));
		
		Movimiento movimiento = new Movimiento();
		movimiento.setCuenta(cuenta);
		movimiento.setMonto(new BigDecimal("500.00"));
		movimiento.setTipoTransaccion(tipoTransaccionInterbancaria);
		
		MovimientoDTO movimientoDTO = new MovimientoDTO();
		movimientoDTO.setMonto(transferenciaDTO.getMonto());
		movimientoDTO.setDetalleMovimiento(detalle);
		
		
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		when(bancoRepositorio.findById(banco2.getId())).thenReturn(Optional.of(banco2));
		when(tipoTransaccionRepositorio.findByNombre("Transferencia_interbancaria")).thenReturn(Optional.of(tipoTransaccionBancaria));		
		when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.of(usuario));
		when(cuentaRepositorio.findById(cuenta.getId())).thenReturn(Optional.of(cuenta));
		

		when(cuentaRepositorio.findByCci(cuenta2.getCci())).thenReturn(Optional.of(cuenta2));
		
		when(movimientoRepositorio.save(any(Movimiento.class))).thenReturn(movimiento);
		
		MovimientoDTO resultado = movimientoService.transferenciaInterbancaria(banco.getId(), usuario.getId(), cuenta.getId(), transferenciaDTO);
		
		assertNotNull(resultado);
		assertEquals("Transferencia_interbancaria", resultado.getTipoTransaccion().getNombre());
		assertEquals(new BigDecimal("500.00"), resultado.getMonto());
		assertEquals(new BigDecimal("2495.00"), cuenta2.getSaldo());
	}
}
