package com.banco.backend.utils;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Rol;
import com.banco.backend.entity.TipoCuenta;
import com.banco.backend.entity.TipoTransaccion;
import com.banco.backend.entity.Usuario;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.RolRepositorio;
import com.banco.backend.repository.TipoCuentaRepositorio;
import com.banco.backend.repository.TipoTransaccionRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;

@Component
public class Iniciando {
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private RolRepositorio rolRepositorio;

	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private TipoTransaccionRepositorio tipoTransaccionRepositorio;
	
	@Autowired
	private TipoCuentaRepositorio tipoCuentaRepositorio;
	
	@Autowired
	private PasswordEncoder encoder;
	
	
	@PostConstruct
	private void crearDatos() {
		
		Rol rolAdmin = rolRepositorio.findByNombre("ROLE_ADMIN").orElse(null);
		if(rolAdmin == null) {
			rolAdmin = new Rol();
			rolAdmin.setNombre("ROLE_ADMIN");
			rolRepositorio.save(rolAdmin);
		}
		
		Rol rolUser = rolRepositorio.findByNombre("ROLE_USER").orElse(null);
		if(rolUser == null) {
			rolUser = new Rol();
			rolUser.setNombre("ROLE_USER");
			rolRepositorio.save(rolUser);
		}
		
		if(usuarioRepositorio.findByUsername("admin") == null) {
			Usuario userAdmin = new Usuario();
			userAdmin.setNombre("admin");
			userAdmin.setApellido("admin");
			userAdmin.setDni(null);
			userAdmin.setEdad(0);
			userAdmin.setGenero(null);
			userAdmin.setCelular(null);
			userAdmin.setEmail("admin@gmail.com");
			userAdmin.setUsername("admin");
			userAdmin.setPassword(encoder.encode("12345"));
			
			Rol rol = rolRepositorio.findByNombre("ROLE_ADMIN").get();
			userAdmin.setRol(Collections.singleton(rol));
			usuarioRepositorio.save(userAdmin);
			
			
		Banco banco = bancoRepositorio.findByNombre("Banco Bambinos").orElse(null);
		if(banco == null) {
			banco = new Banco();
			banco.setNombre("Banco Bambinos");
			bancoRepositorio.save(banco);
		}
		
		TipoTransaccion tipoTransaccion = tipoTransaccionRepositorio.findByNombre("Deposito").orElse(null);
		if(tipoTransaccion == null) {
			tipoTransaccion = new TipoTransaccion();
			tipoTransaccion.setNombre("Deposito");
			tipoTransaccionRepositorio.save(tipoTransaccion);
		}
		
		TipoTransaccion tipoTransaccion2 = tipoTransaccionRepositorio.findByNombre("Retiro").orElse(null);
		if(tipoTransaccion2 == null) {
			tipoTransaccion2 = new TipoTransaccion();
			tipoTransaccion2.setNombre("Retiro");
			tipoTransaccionRepositorio.save(tipoTransaccion2);
		}
		
		TipoTransaccion tipoTransaccion3 = tipoTransaccionRepositorio.findByNombre("Transferencia_bancaria").orElse(null);
		if(tipoTransaccion3 == null) {
			tipoTransaccion3 = new TipoTransaccion();
			tipoTransaccion3.setNombre("Transferencia_bancaria");
			tipoTransaccionRepositorio.save(tipoTransaccion3);
		}
		
		TipoTransaccion tipoTransaccion4 = tipoTransaccionRepositorio.findByNombre("Transferencia_interbancaria").orElse(null);
		if(tipoTransaccion4 == null) {
			tipoTransaccion4 = new TipoTransaccion();
			tipoTransaccion4.setNombre("Transferencia_interbancaria");
			tipoTransaccionRepositorio.save(tipoTransaccion4);
		}
		
		TipoCuenta tipoCuenta = tipoCuentaRepositorio.findByNombre("Cuenta de ahorro").orElse(null);
		if(tipoCuenta == null) {
			tipoCuenta = new TipoCuenta();
			tipoCuenta.setNombre("Cuenta de ahorro");
			tipoCuenta.setDescripcion("Las cuentas de ahorro te permiten generar una pequeña rentabilidad sobre el dinero que guardas en ellas.");
			tipoCuentaRepositorio.save(tipoCuenta);
		}
		
		TipoCuenta tipoCuenta2 = tipoCuentaRepositorio.findByNombre("Cuenta corriente").orElse(null);
		if(tipoCuenta2 == null) {
			tipoCuenta2 = new TipoCuenta();
			tipoCuenta2.setNombre("Cuenta corriente");
			tipoCuenta2.setDescripcion("Te permite manejar tu dinero de manera fácil, sencilla y segura, pues puedes disponer del efectivo cuando lo requieras");
			tipoCuentaRepositorio.save(tipoCuenta2);
		}
		
		TipoCuenta tipoCuenta3 = tipoCuentaRepositorio.findByNombre("Cuenta sueldo").orElse(null);
		if(tipoCuenta3 == null) {
			tipoCuenta3 = new TipoCuenta();
			tipoCuenta3.setNombre("Cuenta sueldo");
			tipoCuenta3.setDescripcion("Son cuentas de ahorros diseñadas para que puedas recibir tu sueldo, la empresa donde trabajas (empleadora) podrá realizar el pago de tu remuneración en la entidad financiera que elijas");
			tipoCuentaRepositorio.save(tipoCuenta3);
		}
	}
	
	}
}
