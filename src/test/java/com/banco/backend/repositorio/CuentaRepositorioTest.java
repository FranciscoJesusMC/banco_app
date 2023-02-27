package com.banco.backend.repositorio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Cuenta;
import com.banco.backend.entity.TipoCuenta;
import com.banco.backend.entity.Usuario;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.CuentaRepositorio;
import com.banco.backend.repository.TipoCuentaRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;

@DataJpaTest
public class CuentaRepositorioTest {
	
	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	
	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private TipoCuentaRepositorio tipoCuentaRepositorio;
	
	private Banco banco;
	
	private TipoCuenta tipoCuenta;
	
	private Usuario usuario;
	
	private Cuenta cuenta;
	
	private Cuenta cuenta2;
	
	@BeforeEach
	void init() {
		
		banco = new Banco();
		banco.setNombre("Banco");
		bancoRepositorio.save(banco);
		
		
		tipoCuenta = new TipoCuenta();
		tipoCuenta.setNombre("Cuenta ahorros");
		tipoCuenta.setDescripcion("xd");
		tipoCuentaRepositorio.save(tipoCuenta);
		
		usuario = new Usuario();
		usuario.setNombre("Francisco");
		usuario.setApellido("Maculunco");
		usuario.setCelular("987776029");
		usuario.setDni("76337194");
		usuario.setEdad(18);
		usuario.setEmail("franck@gmail.com");
		usuarioRepositorio.save(usuario);
		
		cuenta = new Cuenta();
		cuenta.setBanco(banco);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setUsuario(usuario);
		cuenta.setEstado("Inhabilitada");
		cuenta.setSaldo(new BigDecimal("0.00"));
		
		cuenta2 = new Cuenta();
		cuenta2.setBanco(banco);
		cuenta2.setTipoCuenta(tipoCuenta);
		cuenta2.setUsuario(usuario);
		cuenta2.setEstado("Habilitada");
		cuenta2.setSaldo(new BigDecimal("0.00"));
	}
	
	
	@Test
	void crearCuenta() {
		
		Cuenta guardarCuenta = cuentaRepositorio.save(cuenta);
		
		assertNotNull(guardarCuenta);
		assertThat(guardarCuenta.getId()).isNotEqualTo(0);
		assertEquals("Inhabilitada", guardarCuenta.getEstado());
	}
	
	@Test
	void  listarCuenta() {
		
		cuentaRepositorio.save(cuenta);
		cuentaRepositorio.save(cuenta2);
		
		List<Cuenta> lista = cuentaRepositorio.findAll();
		
		assertNotNull(lista);
		assertEquals(2, lista.size());
		
	}
	
	@Test
	void buscarCuentaPorId() {
		
		cuentaRepositorio.save(cuenta);
		
		Cuenta buscarCuenta = cuentaRepositorio.findById(cuenta.getId()).get();
		
		assertNotNull(buscarCuenta);
		assertEquals("Inhabilitada", buscarCuenta.getEstado());
		
		
	}
	
	@Test
	void actualizarCuenta() {
		
		cuentaRepositorio.save(cuenta);
		
		Cuenta buscarCuenta = cuentaRepositorio.findById(cuenta.getId()).get();
		buscarCuenta.setEstado("Habilitada");
		
		Cuenta actualizarCuenta = cuentaRepositorio.save(buscarCuenta);
		
		assertEquals("Habilitada", actualizarCuenta.getEstado());
		
	}
	
	@Test
	void eliminarCuenta() {
		
		cuentaRepositorio.save(cuenta);
		cuentaRepositorio.save(cuenta2);
		
		cuentaRepositorio.delete(cuenta);
		Optional<Cuenta> buscarCuenta = cuentaRepositorio.findById(cuenta.getId());
		
		List<Cuenta> lista = cuentaRepositorio.findAll();
		
		assertNotNull(lista);
		assertEquals(1, lista.size());
		assertThat(buscarCuenta).isEmpty();
	}

}
