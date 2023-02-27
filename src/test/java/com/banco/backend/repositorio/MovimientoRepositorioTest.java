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
import com.banco.backend.entity.Movimiento;
import com.banco.backend.entity.TipoCuenta;
import com.banco.backend.entity.TipoTransaccion;
import com.banco.backend.entity.Usuario;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.CuentaRepositorio;
import com.banco.backend.repository.MovimientoRepositorio;
import com.banco.backend.repository.TipoCuentaRepositorio;
import com.banco.backend.repository.TipoTransaccionRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;


@DataJpaTest
public class MovimientoRepositorioTest {
	
	@Autowired
	private MovimientoRepositorio movimientoRepositorio;

	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private TipoCuentaRepositorio tipoCuentaRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	
	@Autowired
	private TipoTransaccionRepositorio tipoTransaccionRepositorio;
	
	private Banco banco;
	
	private TipoCuenta tipoCuenta;
	
	private Usuario usuario;
	
	private Cuenta cuenta;
	
	private TipoTransaccion tipoTransaccion;
	
	private TipoTransaccion tipoTransaccion2;
	
	private Movimiento movimiento;
	
	private Movimiento movimiento2;
	
	
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
		cuentaRepositorio.save(cuenta);
		
		tipoTransaccion = new TipoTransaccion();
		tipoTransaccion.setNombre("Retiro");
		tipoTransaccionRepositorio.save(tipoTransaccion);
		
		tipoTransaccion2 = new TipoTransaccion();
		tipoTransaccion2.setNombre("Deposito");
		tipoTransaccionRepositorio.save(tipoTransaccion2);
		
		movimiento = new Movimiento();
		movimiento.setCuenta(cuenta);
		movimiento.setTipoTransaccion(tipoTransaccion);
		movimiento.setMonto(new BigDecimal("100.00"));
		
		movimiento2 = new Movimiento();
		movimiento2.setCuenta(cuenta);
		movimiento2.setTipoTransaccion(tipoTransaccion2);
		movimiento2.setMonto(new BigDecimal("500.00"));
		
		
		
	}
	
	@Test
	void crearMovimiento() {
		
		Movimiento guardarMovimiento = movimientoRepositorio.save(movimiento);
		
		assertNotNull(guardarMovimiento);
		assertEquals(cuenta.getId(), guardarMovimiento.getCuenta().getId());
		
	}
	
	
	@Test
	void listarMovimientos() {
		
		movimientoRepositorio.save(movimiento);
		movimientoRepositorio.save(movimiento2);
		
		List<Movimiento> listar = movimientoRepositorio.findAll();
		
		assertEquals(2, listar.size());
		
		
		
	}
	
	@Test
	void buscarMovimiento() {
		
		movimientoRepositorio.save(movimiento);
		
		Movimiento buscarMovimiento = movimientoRepositorio.findById(movimiento.getId()).get();
		
		assertNotNull(buscarMovimiento);
		assertThat(buscarMovimiento.getId()).isNotEqualTo(0);
	}
	
	@Test
	void actualizarMovimiento() {
		
		movimientoRepositorio.save(movimiento);
		
		Movimiento buscarMovimiento = movimientoRepositorio.findById(movimiento.getId()).get();
		buscarMovimiento.setMonto(new BigDecimal("150.00"));
		
		Movimiento actualizarMovimiento = movimientoRepositorio.save(buscarMovimiento);
		
		assertNotNull(actualizarMovimiento);
		assertEquals(new BigDecimal("150.00"), actualizarMovimiento.getMonto());
	}
	
	@Test
	void eliminarMovimiento() {
		
		
		movimientoRepositorio.save(movimiento);
		movimientoRepositorio.save(movimiento2);
		
		movimientoRepositorio.delete(movimiento);
		
		Optional<Movimiento> buscarMovimiento = movimientoRepositorio.findById(movimiento.getId());
		List<Movimiento> lista = movimientoRepositorio.findAll();
		
		assertEquals(1, lista.size());
		assertThat(buscarMovimiento).isEmpty();
	}
}
