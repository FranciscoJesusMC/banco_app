package com.banco.backend.serviceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.CuentaDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Cuenta;
import com.banco.backend.entity.TipoCuenta;
import com.banco.backend.entity.Usuario;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.CuentaMapper;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.CuentaRepositorio;
import com.banco.backend.repository.TipoCuentaRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.service.CuentaService;

@Service
public class CuentaServiceImpl  implements CuentaService{

	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	
	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private TipoCuentaRepositorio tipoCuentaRepositorio;
	
	@Autowired
	private CuentaMapper mapper;

	@Override
	public List<CuentaDTO> listarCuentasPorBancoId(long bancoId) {
		bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<Cuenta> cuentas = cuentaRepositorio.findByBancoId(bancoId);
		
		if(cuentas.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "No hay cuentas registradas en el banco con el id: " +bancoId);
		}
		return cuentas.stream().map(cuenta -> mapper.cuentatoCuentaDTO(cuenta)).collect(Collectors.toList());
	}

	@Override
	public List<CuentaDTO> listarCuentasPorUsuarioId(long bancoId, long usuarioId) {
		bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<Cuenta> cuentas = cuentaRepositorio.findByUsuarioId(usuarioId);
		
		if(cuentas.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "El usuario con el id :" + usuarioId + "no tiene cuentas registradas en el banco con el id : " +bancoId);
			
		}
		return cuentas.stream().map(cuenta -> mapper.cuentatoCuentaDTO(cuenta)).collect(Collectors.toList());
	}

	@Override
	public List<CuentaDTO> listarCuentasPorTipo(long bancoId, long tipoCuentaId) {
		bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<Cuenta> cuentas = cuentaRepositorio.findByTipoCuentaId(tipoCuentaId);
		if(cuentas.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "No hay cuentas con el tipo de cuenta id :"+ tipoCuentaId + " en el banco con el id :" + bancoId);
		}
		
		return cuentas.stream().map(cuenta -> mapper.cuentatoCuentaDTO(cuenta)).collect(Collectors.toList());
		
	}

	@Override
	public CuentaDTO buscarCuentaPorId(long bancoId, UUID cuentaId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta con el id : "+ cuentaId + "no pertenece al banco con el id: " +bancoId );
		}
		return mapper.cuentatoCuentaDTO(cuenta);
	}

	@Override
	public CuentaDTO crearCuenta(long bancoId,long usuarioId, long tipoCuentaId, CuentaDTO cuentaDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		TipoCuenta tipoCuenta = tipoCuentaRepositorio.findById(tipoCuentaId).orElseThrow(()-> new ResourceNotFoundException("TipoCuenta", "id", tipoCuentaId));
		
		List<Cuenta> lista = cuentaRepositorio.findByUsuarioId(usuarioId);
		
		for(int i = 0;i <lista.size();i++) {
			if(lista.get(i).getTipoCuenta().getId().equals(tipoCuentaId)) {
				throw new BancoAppException(HttpStatus.NOT_FOUND, "El usuario con el id :" + usuarioId +" ya tiene un tipo de cuenta con el id :" + tipoCuentaId + " en el banco con el id: " + bancoId);
			}
		}
		
		
		Cuenta cuenta = mapper.cuentaDTOtoCuenta(cuentaDTO);
		
		cuenta.setBanco(banco);
		cuenta.setUsuario(usuario);
		cuenta.setTipoCuenta(tipoCuenta);
		cuenta.setEstado("Habilitado");
		cuenta.setSaldo(0);
		
		Cuenta nuevaCuenta = cuentaRepositorio.save(cuenta);
		
		CuentaDTO guardarCuenta = mapper.cuentatoCuentaDTO(nuevaCuenta);
		
		return guardarCuenta;
	}

	@Override
	public void eliminarCuenta(long bancoId, UUID cuentaId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta con el id : "+ cuentaId + "no pertenece al banco con el id: " +bancoId );
		}
		
		cuentaRepositorio.delete(cuenta);
		
	}

	@Override
	public void deshabilitarCuentaPorUsuario(long bancoId, long usuarioId, UUID cuentaId) {
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getUsuario().getId().equals(usuario.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al usuario");
		}
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta con el id : "+ cuentaId + "no pertenece al banco con el id: " +bancoId );
		}
		
		if(cuenta.getEstado().equals("Inhabilitado")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta actualmente esta inhabilitado");
		}
		
		cuenta.setEstado("Inhabilitado");
		cuentaRepositorio.save(cuenta);
	}

	@Override
	public void deshabilitarCuentaPorAdmin(long bancoId, UUID cuentaId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta con el id : "+ cuentaId + "no pertenece al banco con el id: " +bancoId );
		}
		
		if(cuenta.getEstado().equals("Inhabilitado")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta actualmente esta inhabilitado");
		}
		
		cuenta.setEstado("Inhabilitado");
		cuentaRepositorio.save(cuenta);
		
	}
	
	@Override
	public void habilitarCuentaPorUsuario(long bancoId, long usuarioId, UUID cuentaId) {
	Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getUsuario().getId().equals(usuario.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al usuario");
		}
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta con el id : "+ cuentaId + "no pertenece al banco con el id: " +bancoId );
		}
		
		if(cuenta.getEstado().equals("Habilitado")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta actualmente esta habilitado");
		}
		
		cuenta.setEstado("Habilitado");
		cuentaRepositorio.save(cuenta);
		
	}

	@Override
	public void habilitarCuentaPorAdmin(long bancoId, UUID cuentaId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(() -> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta con el id : "+ cuentaId + "no pertenece al banco con el id: " +bancoId );
		}
		
		if(cuenta.getEstado().equals("Habilitado")) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta actualmente esta habilitado");
		}
		
		cuenta.setEstado("Habilitado");
		cuentaRepositorio.save(cuenta);
		
	}
	
	//Metodo para que los depositos y reitros del dia vuelvan a 0 al finalizar el dia
	@Scheduled(cron = "0 0 0 * * *")
	public void resetearRetirosYDepositosDelDia() {
		List<Cuenta> cuentas = cuentaRepositorio.findAll();
		cuentas.forEach(cuenta ->{
			cuenta.setDepositosDelDia(0);
			cuenta.setRetirorsDelDia(0);
			cuentaRepositorio.save(cuenta);
		});
	}
}
