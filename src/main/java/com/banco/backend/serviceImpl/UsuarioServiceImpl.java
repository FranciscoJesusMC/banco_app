package com.banco.backend.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.UsuarioDTO;
import com.banco.backend.entity.Banco;
import com.banco.backend.entity.Usuario;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.UsuarioMapper;
import com.banco.backend.repository.BancoRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private BancoRepositorio bancoRepositorio;
	
	@Autowired
	private UsuarioMapper mapper;
		
	@Override
	public List<UsuarioDTO> listarUsuariosPorBanco(long bancoId) {
		bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<Usuario> usuarios = usuarioRepositorio.findByBancoId(bancoId);
		
		if(usuarios.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "No hay usuarios registrados en el banco con el id: " +bancoId);
		}
		
		
		return usuarios.stream().map(usuario -> mapper.usuariotoUsuarioDTO(usuario)).collect(Collectors.toList());
	}
	@Override
	public UsuarioDTO crearUsuario(long bancoId,UsuarioDTO usuarioDTO) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<Usuario> listaUsuarios = usuarioRepositorio.findByBancoId(bancoId);
		
		for(int i = 0 ;i <listaUsuarios.size(); i++) {
			if(listaUsuarios.get(i).getDni() == usuarioDTO.getDni()) {
				throw new BancoAppException(HttpStatus.BAD_REQUEST, "El dni :" + usuarioDTO.getDni() + " ya se encuentra registrado");
			}
			if(listaUsuarios.get(i).getEmail().equals(usuarioDTO.getEmail())) {
				throw new BancoAppException(HttpStatus.BAD_REQUEST, "El dni :" + usuarioDTO.getEmail() + " ya se encuentra registrado");
			}
			if(listaUsuarios.get(i).getCelular() == usuarioDTO.getCelular()) {
				throw new BancoAppException(HttpStatus.BAD_REQUEST, "El celular :" + usuarioDTO.getCelular() + " ya se encuentra registrado");
			}			
		}
	
		Usuario usuario = mapper.usuarioDTOtoUsuario(usuarioDTO);
		
		usuario.setBanco(banco);
		Usuario nuevoUsuario =  usuarioRepositorio.save(usuario);
		
		UsuarioDTO guardarUsuario = mapper.usuariotoUsuarioDTO(nuevoUsuario);
		
		return guardarUsuario;
		
	}
	@Override
	public UsuarioDTO buscarUsuarioPorBancoId(long bancoId, long usuarioId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		if(!usuario.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "El usuario : "+ usuarioId + "no pertenece al banco con el id : "+ bancoId );
		}
		
		return mapper.usuariotoUsuarioDTO(usuario);
	}
	@Override
	public UsuarioDTO actualizarUsuario(long bancoId, long usuarioId,UsuarioDTO usuarioDTO) {
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
	
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		List<Usuario> listaUsuarios = usuarioRepositorio.findByBancoId(bancoId);
		
		if(!usuario.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "EL usuario no pertenece al banco");
		}
		
		for(int i = 0 ;i <listaUsuarios.size(); i++) {
			if(listaUsuarios.get(i).getDni().equals(usuarioDTO.getDni())) {
				throw new BancoAppException(HttpStatus.BAD_REQUEST, "El dni :" + usuarioDTO.getDni() + " ya se encuentra registrado");
			}
			if(listaUsuarios.get(i).getEmail().equals(usuarioDTO.getEmail())) {
				throw new BancoAppException(HttpStatus.BAD_REQUEST, "El dni :" + usuarioDTO.getEmail() + " ya se encuentra registrado");
			}
			if(listaUsuarios.get(i).getCelular().equals(usuarioDTO.getCelular())) {
				throw new BancoAppException(HttpStatus.BAD_REQUEST, "El celular :" + usuarioDTO.getCelular() + " ya se encuentra registrado");
			}			
		}
		
		Usuario guardarUsuario = usuarioRepositorio.save(usuario);
		
		UsuarioDTO actualizarUsuario = mapper.usuariotoUsuarioDTO(guardarUsuario);
		
		return actualizarUsuario;
	}
		
			
	
	@Override
	public void eliminarUsuario(long bancoId, long usuarioId) {
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow(()-> new ResourceNotFoundException("Banco", "id", bancoId));
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		if(!usuario.getBanco().getId().equals(banco.getId())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "El usuario : "+ usuarioId + "no pertenece al banco con el id : "+ bancoId );
		}
		
		usuarioRepositorio.delete(usuario);
	}
	@Override
	public UsuarioDTO buscarUsuario(long usuarioId) {
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		return mapper.usuariotoUsuarioDTO(usuario);
	}

}
