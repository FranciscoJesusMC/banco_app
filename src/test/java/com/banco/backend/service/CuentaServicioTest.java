package com.banco.backend.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Cuenta;
import com.banco.backend.entity.TipoCuenta;
import com.banco.backend.entity.Usuario;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.CuentaRepositorio;
import com.banco.backend.repository.TipoCuentaRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.serviceImpl.CuentaServiceImpl;
import com.banco.backend.utils.Genero;

@ExtendWith(MockitoExtension.class)
public class CuentaServicioTest {

	private CuentaRepositorio cuentaRepositorio = Mockito.mock(CuentaRepositorio.class);

	private BancoRepositorio bancoRepositorio = Mockito.mock(BancoRepositorio.class);
	
	private TipoCuentaRepositorio tipoCuentaRepositorio = Mockito.mock(TipoCuentaRepositorio.class);
	
	private UsuarioRepositorio usuarioRepositorio = Mockito.mock(UsuarioRepositorio.class);
	
	@InjectMocks
	private CuentaServiceImpl cuentaService = new CuentaServiceImpl();

	
	@Test
	void crearCuenta() {
		
		TipoCuenta tipoCuenta = new TipoCuenta();
		tipoCuenta.setId(1L);
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");

		Banco banco = new Banco();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
	
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");

		Cuenta cuenta = new Cuenta();
		cuenta.setBanco(banco);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setUsuario(usuario);

		
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.of(usuario));
		when(tipoCuentaRepositorio.findById(anyLong())).thenReturn(Optional.of(tipoCuenta));
		when(cuentaRepositorio.save(any(Cuenta.class))).thenReturn(cuenta);
		
		CuentaDTO resultado = cuentaService.crearCuenta(banco.getId(), usuario.getId(), tipoCuenta.getId());
		
		assertNotNull(resultado);

		
	}
	
	@Test
	void buscarCuentaPorId() {
		
		TipoCuenta tipoCuenta = new TipoCuenta();
		tipoCuenta.setId(1L);
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");

		Banco banco = new Banco();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
	
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");

		Cuenta cuenta = new Cuenta();
		cuenta.setBanco(banco);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setUsuario(usuario);

		
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.of(usuario));
		when(tipoCuentaRepositorio.findById(anyLong())).thenReturn(Optional.of(tipoCuenta));
		
		
		when(cuentaRepositorio.findById(cuenta.getId())).thenReturn(Optional.of(cuenta));
		
		CuentaDTO cuentaDTO = cuentaService.buscarCuentaPorId(banco.getId(), cuenta.getId());
		
		assertNotNull(cuentaDTO);
		assertEquals(1,cuentaDTO.getBanco().getId());
		assertEquals(1, cuentaDTO.getTipoCuenta().getId());
		
	}
	
	@Test
	void listarCuentasPorUsuario() {
		
		TipoCuenta tipoCuenta = new TipoCuenta();
		tipoCuenta.setId(1L);
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");
		
		TipoCuenta tipoCuenta2 = new TipoCuenta();
		tipoCuenta2.setId(2L);
		tipoCuenta2.setNombre("Cuenta de prueba2");
		tipoCuenta2.setDescripcion("hola2");

		Banco banco = new Banco();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
	
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");

		Cuenta cuenta = new Cuenta();
		cuenta.setBanco(banco);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setUsuario(usuario);
		
		Cuenta cuenta2 = new Cuenta();
		cuenta2.setBanco(banco);
		cuenta2.setTipoCuenta(tipoCuenta2);
		cuenta2.setUsuario(usuario);
		
		List<Cuenta> cuentas = new ArrayList<>();
		cuentas.add(cuenta);
		cuentas.add(cuenta2);
		
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.of(usuario));
		when(tipoCuentaRepositorio.findById(anyLong())).thenReturn(Optional.of(tipoCuenta));
		
		when(cuentaRepositorio.findByUsuarioIdAndBancoId(usuario.getId(), banco.getId())).thenReturn(cuentas);
		
		List<CuentaDTO> lista= cuentaService.listarCuentasPorUsuarioId(banco.getId(), usuario.getId());
		
		assertNotNull(lista);
		assertEquals(2, lista.size());
		
		
	}
	
	@Test
	void deshabilitarCuentaPorUsuario() {
		
		TipoCuenta tipoCuenta = new TipoCuenta();
		tipoCuenta.setId(1L);
		tipoCuenta.setNombre("Cuenta de prueba");
		tipoCuenta.setDescripcion("hola");

		Banco banco = new Banco();
		banco.setId(1L);
		banco.setNombre("Banco nuevo");
	
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setGenero(Genero.M);
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");

		Cuenta cuenta = new Cuenta();
		cuenta.setBanco(banco);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setUsuario(usuario);
		cuenta.setEstado("Habilitada");
		
		when(bancoRepositorio.findById(anyLong())).thenReturn(Optional.of(banco));
		when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.of(usuario));
		when(tipoCuentaRepositorio.findById(anyLong())).thenReturn(Optional.of(tipoCuenta));
		
		when(cuentaRepositorio.findById(cuenta.getId())).thenReturn(Optional.of(cuenta));
		
		cuentaService.deshabilitarCuentaPorUsuario(banco.getId(), usuario.getId(), cuenta.getId());
		
		assertEquals("Deshabilitada", cuenta.getEstado());
		
	}
}
