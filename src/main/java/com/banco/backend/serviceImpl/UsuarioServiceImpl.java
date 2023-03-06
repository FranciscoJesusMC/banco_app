package com.banco.backend.serviceImpl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banco.backend.dto.UsuarioDTO;
import com.banco.backend.entity.Rol;
import com.banco.backend.entity.Usuario;
import com.banco.backend.excepciones.BancoAppException;
import com.banco.backend.excepciones.ResourceNotFoundException;
import com.banco.backend.mapper.UsuarioMapper;
import com.banco.backend.mapper.UsuarioMapperImpl;
import com.banco.backend.repository.RolRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private RolRepositorio rolRepositorio;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UsuarioMapper mapper = new UsuarioMapperImpl();
		

	@Override
	public List<UsuarioDTO> listarUsuariosPorBanco() {
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		
		if(usuarios.isEmpty()) {
			throw new BancoAppException(HttpStatus.NOT_FOUND, "No hay usuarios registrados ");
		}
		
		return usuarios.stream().map(usuario -> mapper.usuariotoUsuarioDTO(usuario)).collect(Collectors.toList());
	}
	@Override
	public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {	
		Usuario usuario = mapper.usuarioDTOtoUsuario(usuarioDTO);
		
		if(usuarioRepositorio.existsByUsername(usuarioDTO.getUsername())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "El username :" + usuarioDTO.getUsername() + " ya se encuentra registrado");
		}
		
		if(usuarioRepositorio.existsByDni(usuarioDTO.getDni())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "El dni :" + usuarioDTO.getDni() + " ya se encuentra registrado");
		}
		
		if(usuarioRepositorio.existsByEmail(usuarioDTO.getEmail())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "EL email : " + usuarioDTO.getEmail() + " ya se encuentra registrado");
		}
		
		if(usuarioRepositorio.existsByCelular(usuarioDTO.getCelular())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "EL celular : " + usuarioDTO.getCelular() + " ya se encuentra registrado");
		}
	
		usuario.setPassword(encoder.encode(usuarioDTO.getPassword()));
		
		Rol rol = rolRepositorio.findByNombre("ROLE_USER").get();
		usuario.setRol(Collections.singleton(rol));
		
		Usuario nuevoUsuario =  usuarioRepositorio.save(usuario);
	
		
		UsuarioDTO guardarUsuario = mapper.usuariotoUsuarioDTO(nuevoUsuario);
		
		return guardarUsuario;
		
	}

	@Override
	public UsuarioDTO actualizarUsuario(long usuarioId,UsuarioDTO usuarioDTO) {
		
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
			
		if(usuarioRepositorio.existsByUsername(usuarioDTO.getUsername())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "El username :" + usuarioDTO.getUsername() + " ya se encuentra registrado");
		}
		
		if(usuarioRepositorio.existsByDni(usuarioDTO.getDni())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "El dni :" + usuarioDTO.getDni() + " ya se encuentra registrado");
		}
		
		if(usuarioRepositorio.existsByEmail(usuarioDTO.getEmail())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "EL email : " + usuarioDTO.getEmail() + " ya se encuentra registrado");
		}
		
		if(usuarioRepositorio.existsByCelular(usuarioDTO.getCelular())) {
			throw new BancoAppException(HttpStatus.BAD_REQUEST, "EL celular : " + usuarioDTO.getCelular() + " ya se encuentra registrado");
		}
		
		usuario.setUsername(usuario.getUsername());
		
		usuario.setNombre(usuarioDTO.getNombre());
		usuario.setApellido(usuarioDTO.getApellido());
		usuario.setEdad(usuarioDTO.getEdad());
		usuario.setCelular(usuarioDTO.getCelular());
		usuario.setDni(usuarioDTO.getDni());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setPassword(encoder.encode(usuarioDTO.getPassword()));

		Usuario guardarUsuario = usuarioRepositorio.save(usuario);
		
		UsuarioDTO actualizarUsuario = mapper.usuariotoUsuarioDTO(guardarUsuario);
		
		return actualizarUsuario;
	}
		
			
	
	@Override
	public void eliminarUsuario( long usuarioId) {
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
	
		usuarioRepositorio.delete(usuario);
	}
	
	
	@Override
	public UsuarioDTO buscarUsuario(long usuarioId) {
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", usuarioId));
		
		return mapper.usuariotoUsuarioDTO(usuario);
	}

}
