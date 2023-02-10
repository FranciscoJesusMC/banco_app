package com.banco.backend.serviceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.MovimientoDTO;
import com.banco.backend.dto.TransferenciaDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Cuenta;
import com.banco.backend.entity.DetalleMovimiento;
import com.banco.backend.entity.Movimiento;
import com.banco.backend.entity.TipoTransaccion;
import com.banco.backend.entity.Usuario;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.MovimientoMapper;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.CuentaRepositorio;
import com.banco.backend.repository.DetalleMovimientoRepositorio;
import com.banco.backend.repository.MovimientoRepositorio;
import com.banco.backend.repository.TipoTransaccionRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.service.MovimientoService;

@Service
public class MovimientoServiceImpl implements MovimientoService {
	
	@Autowired
	private MovimientoRepositorio movimientosRepositorio;
	
	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	
	@Autowired
	private TipoTransaccionRepositorio tipoTransaccionRepositorio;
	
	@Autowired
	private DetalleMovimientoRepositorio detalleMovimientoRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private MovimientoMapper mapper;

	@Override
	public List<MovimientoDTO> listarMovimientosPorCuenta(long bancoId,UUID cuentaId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		List<Movimiento> movimientos = movimientosRepositorio.findByCuentaId(cuentaId);
		
		if(movimientos.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "La cuenta con el id: "+ cuentaId +" no tiene movimientos");
		}
		return movimientos.stream().map(movimiento -> mapper.movimientotoMovimientoDTO(movimiento)).collect(Collectors.toList());
	}

	@Override
	public MovimientoDTO agregarSaldo(long bancoId,UUID cuentaId,MovimientoDTO movimientosDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		if(cuenta.getEstado().equals("Inhabilitado")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta inhabilitada");
		}
		
		float nuevoSaldo = cuenta.getSaldo() + movimientosDTO.getMonto();
		cuenta.setSaldo(nuevoSaldo);
		cuenta.setDepositosDelDia(cuenta.getDepositosDelDia() + 1);
		cuentaRepositorio.save(cuenta);
			
		
		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitularDestino(cuenta.getUsuario().getNombre());
		detalle.setTitular(null);
		detalle.setBancoOrigen(null);
		detalle.setCuentaOrigen(null);
		detalle.setCuentaDestino(cuentaId);
		detalle.setBancoDestino(banco.getNombre());
		detalle.setComision(0);
		detalle.setMontoTotal(movimientosDTO.getMonto());
		detalleMovimientoRepositorio.save(detalle);
		
		
		Movimiento movimiento = mapper.movimientoDTOtoMovimiento(movimientosDTO);
		movimiento.setCuenta(cuenta);
		movimiento.setDetalleMovimiento(detalle);

		TipoTransaccion tipo = tipoTransaccionRepositorio.findByNombre("Deposito").get();
		movimiento.setTipoTransaccion(tipo);
		
		Movimiento nuevoMovimiento = movimientosRepositorio.save(movimiento);
		
		MovimientoDTO guardarMovimiento = mapper.movimientotoMovimientoDTO(nuevoMovimiento);
		
		return guardarMovimiento;
	}

	@Override
	public MovimientoDTO retiro(long bancoId,long usuarioId, UUID cuentaId, MovimientoDTO movimientosDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		if(!cuenta.getUsuario().getId().equals(usuario.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta : " + cuentaId + "  no pertenece al usuarioId : +" + usuarioId);
		}
		
		if(cuenta.getEstado().equals("Inhabilitado")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta inhabilitada");
		}
		
		if(cuenta.getSaldo() < movimientosDTO.getMonto()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no posee el saldo suficiente");
		}
		
	
		
		float nuevoSaldo = cuenta.getSaldo() - movimientosDTO.getMonto();
		cuenta.setSaldo(nuevoSaldo);
		cuenta.setRetirorsDelDia(cuenta.getRetirorsDelDia() + 1);
		cuentaRepositorio.save(cuenta);
		
		
		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitular(cuenta.getUsuario().getNombre());
		detalle.setCuentaOrigen(cuentaId);
		detalle.setCuentaDestino(null);
		detalle.setBancoOrigen(cuenta.getBanco().getNombre());
		detalle.setBancoDestino(null);
		detalle.setTitularDestino(null);
		detalle.setComision(0);
		detalle.setMontoTotal(movimientosDTO.getMonto());
		detalleMovimientoRepositorio.save(detalle);
		
		Movimiento movimiento = mapper.movimientoDTOtoMovimiento(movimientosDTO);
		movimiento.setCuenta(cuenta);
		movimiento.setMonto(movimientosDTO.getMonto());
		movimiento.setDetalleMovimiento(detalle);
		
		TipoTransaccion tipo = tipoTransaccionRepositorio.findByNombre("Retiro").get();
		movimiento.setTipoTransaccion(tipo);
		
		Movimiento nuevoMovimiento = movimientosRepositorio.save(movimiento);
		
		MovimientoDTO guardarMovimiento = mapper.movimientotoMovimientoDTO(nuevoMovimiento);
		
		return guardarMovimiento;
	}

	@Override
	public MovimientoDTO transferenciaBancaria(long bancoId,long usuarioId ,UUID cuentaId, TransferenciaDTO transferenciaDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Cuenta cuentaOrigen = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Cuenta cuentaDestino = cuentaRepositorio.findById(transferenciaDTO.getCuentaDestino()).orElseThrow(()-> new ResourceNotFoundException("Cuenta destino", "id", transferenciaDTO.getCuentaDestino()));
		
		
		if(cuentaOrigen.getEstado().equals("Inhabilitado")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta inhabilitada");
		}
		
		if(!cuentaOrigen.getUsuario().getId().equals(usuario.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta : " + cuentaId + "  no pertenece al usuarioId : +" + usuarioId);
		}
		
		if(!cuentaOrigen.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta origen no pertenece al banco con el id : " + bancoId);
		}
		
		if(!cuentaDestino.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta destino no pertenece al banco con el id : " + bancoId);	
		}
		
		if(cuentaOrigen.getSaldo() < transferenciaDTO.getMonto()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "La cuenta Origen no posee el saldo suficiente");
		}
		
		float restarSaldo = cuentaOrigen.getSaldo() - transferenciaDTO.getMonto();
		cuentaOrigen.setSaldo(restarSaldo);
		cuentaRepositorio.save(cuentaOrigen);
		
		float agregarSaldo = cuentaDestino.getSaldo() + transferenciaDTO.getMonto();
		cuentaDestino.setSaldo(agregarSaldo);
		cuentaRepositorio.save(cuentaDestino);
		
		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitular(cuentaOrigen.getUsuario().getNombre());
		detalle.setCuentaOrigen(cuentaOrigen.getId());
		detalle.setBancoOrigen(cuentaOrigen.getBanco().getNombre());
		detalle.setTitularDestino(cuentaDestino.getUsuario().getNombre());
		detalle.setCuentaDestino(cuentaDestino.getId());
		detalle.setBancoDestino(cuentaDestino.getBanco().getNombre());
		detalle.setComision(0);
		detalle.setMontoTotal(transferenciaDTO.getMonto());
		detalleMovimientoRepositorio.save(detalle);
		
		Movimiento movimiento = new Movimiento();
		movimiento.setCuenta(cuentaOrigen);
		movimiento.setMonto(transferenciaDTO.getMonto());
		movimiento.setDetalleMovimiento(detalle);
		
		TipoTransaccion tipo = tipoTransaccionRepositorio.findByNombre("Transferencia_bancaria").get();
		movimiento.setTipoTransaccion(tipo);
		
		Movimiento nuevoMovimiento = movimientosRepositorio.save(movimiento);
		
		MovimientoDTO guardarMovimiento = mapper.movimientotoMovimientoDTO(nuevoMovimiento);
		
		return guardarMovimiento;
	}

	@Override
	public MovimientoDTO transferenciaInterbancaria(long bancoId,long usuarioId,UUID cuentaId, TransferenciaDTO transferenciaDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Cuenta cuentaOrigen = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Cuenta cuentaDestino = cuentaRepositorio.findById(transferenciaDTO.getCuentaDestino()).orElseThrow(()-> new ResourceNotFoundException("Cuenta destino", "id", transferenciaDTO.getCuentaDestino()));
		
		Banco bancoDestino = bancoRepositorio.findById(cuentaDestino.getBanco().getId()).orElseThrow(()-> new ResourceNotFoundException("Banco destino", "id", cuentaDestino.getBanco().getId()));
		
		if(cuentaOrigen.getEstado().equals("Inhabilitado")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta inhabilitada");
		}
		
		if(!cuentaOrigen.getUsuario().getId().equals(usuario.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta : " + cuentaId + "  no pertenece al usuarioId : +" + usuarioId);
		}
		
		if(!cuentaOrigen.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta origen no pertenece al banco con el id : " + bancoId);
		}
		
		if(!cuentaDestino.getBanco().getId().equals(bancoDestino.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta Destino no pertenece al banco con el id : " + bancoDestino);
			
		}
		
		//Que la cuenta desitno no pertenezca al mismo banco de la cuenta Origen
		if(cuentaDestino.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta destino no debe pertenecer al mismo banco que la cuenta origen");	
		}
		
		if(cuentaOrigen.getSaldo() < transferenciaDTO.getMonto()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "La cuenta Origen no posee el saldo suficiente");
		}
		
		float comision = 5;
		
		float restarSaldo = cuentaOrigen.getSaldo() - transferenciaDTO.getMonto();
		cuentaOrigen.setSaldo(restarSaldo);
		cuentaRepositorio.save(cuentaOrigen);
		
		float agregarSaldo = (cuentaDestino.getSaldo() + transferenciaDTO.getMonto()) - comision;
		cuentaDestino.setSaldo(agregarSaldo);
		cuentaRepositorio.save(cuentaDestino);
		

		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitular(cuentaOrigen.getUsuario().getNombre());
		detalle.setCuentaOrigen(cuentaOrigen.getId());
		detalle.setBancoOrigen(cuentaOrigen.getBanco().getNombre());
		detalle.setTitularDestino(cuentaDestino.getUsuario().getNombre());
		detalle.setCuentaDestino(cuentaDestino.getId());
		detalle.setBancoDestino(cuentaDestino.getBanco().getNombre());
		detalle.setComision(comision);
		detalle.setMontoTotal(transferenciaDTO.getMonto() - comision);
		detalleMovimientoRepositorio.save(detalle);
		
		Movimiento movimiento = new Movimiento();
		movimiento.setCuenta(cuentaOrigen);
		movimiento.setMonto(transferenciaDTO.getMonto());
		movimiento.setDetalleMovimiento(detalle);
		
		TipoTransaccion tipo = tipoTransaccionRepositorio.findByNombre("Transferencia_interbancaria").get();
		movimiento.setTipoTransaccion(tipo);
		
		Movimiento nuevoMovimiento = movimientosRepositorio.save(movimiento);
		
		MovimientoDTO guardarMovimiento = mapper.movimientotoMovimientoDTO(nuevoMovimiento);
		
		return guardarMovimiento;
	}

}
