package com.banco.backend.serviceImpl;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.backend.entity.AccionesAdmin;
import com.banco.backend.entity.Rol;
import com.banco.backend.entity.Usuario;
import com.banco.backend.repository.AccionesAdminRepositorio;
import com.banco.backend.repository.UsuarioRepositorio;
import com.banco.backend.utils.ObtenerAuthenticacion;

@Service
public class AccionesAdminServiceImpl {
	
	@Autowired
	private AccionesAdminRepositorio accionesRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private ObtenerAuthenticacion obtenerAuth;
	
	
	public void realizarAccion(String tipo,long entidadId,UUID cuentaId,String descripcion) {
		Usuario usuario = usuarioRepositorio.findByEmail(obtenerAuth.getAuthentication().getName());
		AccionesAdmin accion = new AccionesAdmin(usuario,tipo, entidadId,cuentaId, descripcion, new Date());
		
		Set<Rol> roles = usuario.getRol();
		
		for(Rol role :roles) {
			if(role.getNombre()=="ROLE_ADMIN") {
				accionesRepositorio.save(accion);
				break;
			}
		}
	}
}
