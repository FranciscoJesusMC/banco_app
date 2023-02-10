package com.banco.backend.serviceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.PrestamoDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Cuenta;
import com.banco.backend.entity.Prestamo;
import com.banco.backend.entity.Usuario;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.PrestamoMapper;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.CuentaRepositorio;
import com.banco.backend.repository.PrestamoRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.service.PrestamoService;

@Service
public class PrestamoServiceImpl implements PrestamoService {

	@Autowired
	private PrestamoRepositorio prestamoRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private CuentaRepositorio cuentaRepositorio;

	@Autowired
	private PrestamoMapper mapper;
	
	
	@Override
	public List<PrestamoDTO> listarPrestamosPorUsuario(long bancoId,long usuarioId, UUID cuentaId) {
		
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		if(!cuenta.getUsuario().getId().equals(usuario.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta : " + cuentaId + "  no pertenece al usuarioId : +" + usuarioId);
		}
				
		List<Prestamo> prestamos = prestamoRepositorio.findByCuentaId(cuentaId);
		if(prestamos.isEmpty()) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST,"La cuenta no tiene prestamos");
		}
		return prestamos.stream().map(prestamo -> mapper.prestamotoPrestamoDTO(prestamo)).collect(Collectors.toList());
	}

	@Override
	public PrestamoDTO generarPrestamo(long bancoId, long usuarioId, UUID cuentaId,PrestamoDTO prestamoDTO) {

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
		
		Prestamo prestamo = mapper.prestamoDTOtoPrestamo(prestamoDTO);
		
		float interes = 5;
		
		prestamo.setImporte(prestamoDTO.getImporte());
		prestamo.setCuotas(prestamoDTO.getCuotas());
		prestamo.setTasaDeInteres(interes);
		
		float interesaP = ((prestamoDTO.getImporte() * prestamoDTO.getCuotas()) *(interes/100));
		
		prestamo.setInteresApagar(interesaP);
		
		float cuotaM = ((prestamoDTO.getImporte() / prestamoDTO.getCuotas()) + (interesaP/prestamoDTO.getCuotas()));
		
		prestamo.setCuotaMensual(cuotaM);
		
		float total = (prestamoDTO.getImporte() +  interesaP);
		
		prestamo.setDeudaTotal(total);
		prestamo.setCuenta(cuenta);
		prestamo.setEstado("Pendiente");
		
		Prestamo guardarPrestamo = prestamoRepositorio.save(prestamo);
		
		PrestamoDTO generarPrestamo = mapper.prestamotoPrestamoDTO(guardarPrestamo);
		
		return generarPrestamo;
	}

	@Override
	public void aprobarPrestamo(long bancoId, UUID cuentaId,long prestamoId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Prestamo prestamo = prestamoRepositorio.findById(prestamoId).orElseThrow(()-> new ResourceNotFoundException("Prestamo", "id", prestamoId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		if(!prestamo.getCuenta().getId().equals(cuenta.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La prestamo : " + prestamoId + "  no pertenece a la cuenta : +" + cuentaId);
		}
		

		prestamo.setEstado("Aprobado");
		prestamoRepositorio.save(prestamo);
		
		float nuevoSaldo =  prestamo.getImporte() +cuenta.getSaldo();
		cuenta.setSaldo(nuevoSaldo);
		
		cuentaRepositorio.save(cuenta);
		
	}

	@Override
	public void rechazarPrestamo(long bancoId, UUID cuentaId, long prestamoId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow(()-> new ResourceNotFoundException("Cuenta", "id", cuentaId));
		
		Prestamo prestamo = prestamoRepositorio.findById(prestamoId).orElseThrow(()-> new ResourceNotFoundException("Prestamo", "id", prestamoId));
		
		if(!cuenta.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La cuenta no pertenece al banco");
		}
		
		if(!prestamo.getCuenta().getId().equals(cuenta.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "La prestamo : " + prestamoId + "  no pertenece a la cuenta : +" + cuentaId);
		}
		
		prestamo.setEstado("Rechazado");
		prestamoRepositorio.save(prestamo);
		
		
		
	}
}
