package com.banco.backend.serviceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.backend.dto.MovimientoDTO;
import com.banco.backend.dto.PaginacionMovimiento;
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
	@Transactional
	public MovimientoDTO agregarSaldo(long bancoId,UUID cuentaId,MovimientoDTO movimientosDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		BigDecimal cuentaSaldo = new BigDecimal(cuenta.getSaldo().toString());
		BigDecimal monto = new BigDecimal(movimientosDTO.getMonto().toString());
		BigDecimal montoMinimo = BigDecimal.ZERO;
		
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		if(cuenta.getEstado().equals("Deshabilitada")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta Deshabilitada");
		}
		
		if(monto.compareTo(montoMinimo)  == 0.00) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "EL monto ingresado debe ser mayor que 0.00");
		}
		
		
		BigDecimal comision = new BigDecimal("0.00");
		BigDecimal nuevoSaldo = cuentaSaldo.add(monto);
		
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
		detalle.setComision(comision);
		detalle.setMontoTotal(monto);
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
	@Transactional
	public MovimientoDTO retiro(long bancoId,long usuarioId, UUID cuentaId, MovimientoDTO movimientosDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		
		BigDecimal saldoCuenta = new BigDecimal(cuenta.getSaldo().toString());
		BigDecimal monto = new BigDecimal(movimientosDTO.getMonto().toString());
		
		int comparacion = saldoCuenta.compareTo(monto);
		
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		if(!cuenta.getUsuario().getId().equals(usuario.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta : " + cuentaId + "  no pertenece al usuarioId : +" + usuarioId);
		}
		
		if(cuenta.getEstado().equals("Deshabilitada")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta Deshabilitada");
		}
		
		if(comparacion < 0) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no posee el saldo suficiente");
		}
		
		if(cuenta.getLimiteDelDia().compareTo(monto) < 0) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta excedio el  monto limite de retiro por dia que es 500.00");
		}
				
		
		BigDecimal comision = new BigDecimal("0.00");
		BigDecimal nuevoSaldo = saldoCuenta.subtract(monto);
		
		
		cuenta.setSaldo(nuevoSaldo);
		cuenta.setRetirorsDelDia(cuenta.getRetirorsDelDia() + 1);
		cuenta.setLimiteDelDia(cuenta.getLimiteDelDia().subtract(monto));
		cuentaRepositorio.save(cuenta);
		
		
		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitular(cuenta.getUsuario().getNombre());
		detalle.setCuentaOrigen(cuentaId);
		detalle.setCuentaDestino(null);
		detalle.setBancoOrigen(cuenta.getBanco().getNombre());
		detalle.setBancoDestino(null);
		detalle.setTitularDestino(null);
		detalle.setComision(comision);
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
	@Transactional
	public MovimientoDTO transferenciaBancaria(long bancoId,long usuarioId ,UUID cuentaId, TransferenciaDTO transferenciaDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Cuenta cuentaOrigen = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Cuenta cuentaDestino = cuentaRepositorio.findById(transferenciaDTO.getCuentaDestino()).orElseThrow(()-> new ResourceNotFoundException("Cuenta destino", "id", transferenciaDTO.getCuentaDestino()));
	
		BigDecimal saldoCuentaOrigen = new BigDecimal(cuentaOrigen.getSaldo().toString());
		BigDecimal saldoCuentaDestino = new BigDecimal(cuentaDestino.getSaldo().toString());
		BigDecimal monto = new BigDecimal(transferenciaDTO.getMonto().toString());
		BigDecimal comision = new BigDecimal("0.00");
		
		
		if(cuentaOrigen.getEstado().equals("Deshabilitada")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta Deshabilitada");
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
		
		if(saldoCuentaOrigen.compareTo(monto) < 0) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "La cuenta Origen no posee el saldo suficiente");
		}
		

		BigDecimal restarSaldo = saldoCuentaOrigen.subtract(monto);
		
		cuentaOrigen.setSaldo(restarSaldo);
		cuentaRepositorio.save(cuentaOrigen);
		
		BigDecimal agregarSaldo = saldoCuentaDestino.add(monto);
		
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
	@Transactional
	public MovimientoDTO transferenciaInterbancaria(long bancoId,long usuarioId,UUID cuentaId, TransferenciaDTO transferenciaDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Cuenta cuentaOrigen = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Cuenta cuentaDestino = cuentaRepositorio.findById(transferenciaDTO.getCuentaDestino()).orElseThrow(()-> new ResourceNotFoundException("Cuenta destino", "id", transferenciaDTO.getCuentaDestino()));
		
		Banco bancoDestino = bancoRepositorio.findById(cuentaDestino.getBanco().getId()).orElseThrow(()-> new ResourceNotFoundException("Banco destino", "id", cuentaDestino.getBanco().getId()));
		
		
		BigDecimal saldoCuentaOrigen = new BigDecimal(cuentaOrigen.getSaldo().toString());
		BigDecimal saldoCuentaDestino = new BigDecimal(cuentaDestino.getSaldo().toString());
		BigDecimal monto = new BigDecimal(transferenciaDTO.getMonto().toString());
		BigDecimal comision = new BigDecimal("5.00");
		
		if(cuentaOrigen.getEstado().equals("Deshabilitada")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta esta Deshabilitada");
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
		
		if(saldoCuentaOrigen.compareTo(monto) < 0) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "La cuenta Origen no posee el saldo suficiente");
		}
		
		
		BigDecimal restarSaldo = saldoCuentaOrigen.subtract(monto);
		cuentaOrigen.setSaldo(restarSaldo);
		cuentaRepositorio.save(cuentaOrigen);
				
		BigDecimal saldoSinComision = saldoCuentaDestino.add(monto);
		BigDecimal nuevoSaldo = saldoSinComision.subtract(comision);
		
		cuentaDestino.setSaldo(nuevoSaldo);
		cuentaRepositorio.save(cuentaDestino);
		
		DetalleMovimiento detalle = new DetalleMovimiento();
		detalle.setTitular(cuentaOrigen.getUsuario().getNombre());
		detalle.setCuentaOrigen(cuentaOrigen.getId());
		detalle.setBancoOrigen(cuentaOrigen.getBanco().getNombre());
		detalle.setTitularDestino(cuentaDestino.getUsuario().getNombre());
		detalle.setCuentaDestino(cuentaDestino.getId());
		detalle.setBancoDestino(cuentaDestino.getBanco().getNombre());
		detalle.setComision(comision);
		detalle.setMontoTotal(monto.subtract(comision));
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

	@Override
	public PaginacionMovimiento paginarMovimientos(int numeroDepagina, int medidaDePagina,String ordenarPor, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(ordenarPor).ascending():Sort.by(ordenarPor).descending();
		Pageable pageable =  PageRequest.of(numeroDepagina, medidaDePagina,sort);
		Page<Movimiento> movimientos = movimientosRepositorio.findAll(pageable);
		
		List<Movimiento> listaMovimientos = movimientos.getContent();
		
		List<MovimientoDTO> contenido = listaMovimientos.stream().map(movimiento ->mapper.movimientotoMovimientoDTO(movimiento)).collect(Collectors.toList());
		
		PaginacionMovimiento respuesta = new PaginacionMovimiento();
		respuesta.setContenido(contenido);
		respuesta.setNumeroDePagina(movimientos.getNumber());
		respuesta.setMedidaDePagina(movimientos.getSize());
		respuesta.setTotalElementos(movimientos.getTotalElements());
		respuesta.setTotalPaginas(movimientos.getTotalPages());
		respuesta.setUltima(movimientos.isLast());
		
		
		return respuesta;
	}

	@Override
	public List<MovimientoDTO> litarMovimientosRecientes(long bancoId, UUID cuentaId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		List<Movimiento> movimientos = movimientosRepositorio.findAllByCuentaIdOrderByFechaCreacionDesc(cuentaId);
		
		if(movimientos.isEmpty()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no tiene movimientos");
		}
		
		return movimientos.stream().map(movimiento ->mapper.movimientotoMovimientoDTO(movimiento)).collect(Collectors.toList());
	}

}
