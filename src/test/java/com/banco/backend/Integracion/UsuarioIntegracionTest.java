package com.banco.backend.Integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.banco.backend.dto.UsuarioDTO;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioIntegracionTest {

	private String baseurl = "http://localhost:8081/api";
	
	private static RestTemplate restTemplate;
	
    
	@BeforeEach
	public void setUp() {
	        restTemplate = new RestTemplate();
	 }
		
	@Test
	void crearUsuario() {
		
			UsuarioDTO usuarioDTO = new UsuarioDTO();
	        usuarioDTO.setNombre("Juan");
	        usuarioDTO.setApellido("Perez");
	        usuarioDTO.setDni("12345678");
	        usuarioDTO.setEdad(30);
	        usuarioDTO.setGenero("M");
	        usuarioDTO.setEmail("juanperez@gmail.com");
	        usuarioDTO.setCelular("123456789");

	        ResponseEntity<UsuarioDTO> response = restTemplate.postForEntity(baseurl+"/crearUsuario", usuarioDTO, UsuarioDTO.class);

	        assertEquals(HttpStatus.CREATED, response.getStatusCode());
	        assertNotNull(response.getBody());
	        assertEquals(usuarioDTO.getNombre(), response.getBody().getNombre());
	        assertEquals(usuarioDTO.getApellido(), response.getBody().getApellido());
	        assertEquals(usuarioDTO.getDni(), response.getBody().getDni());
	        assertEquals(usuarioDTO.getEdad(), response.getBody().getEdad());
	        assertEquals(usuarioDTO.getGenero(), response.getBody().getGenero());
	        assertEquals(usuarioDTO.getEmail(), response.getBody().getEmail());
	        assertEquals(usuarioDTO.getCelular(), response.getBody().getCelular());
	}
	
	@Test
	void listarUsuarios() {
		
//		
//	    ResponseEntity<List<UsuarioDTO>> response = restTemplate.exchange(baseurl + "/listarUsuarios",HttpMethod.GET,null,
//	    		new ParameterizedTypeReference<List<UsuarioDTO>>() { });
		
		UsuarioDTO[] usuarios = restTemplate.getForObject(baseurl + "/listarUsuarios", UsuarioDTO[].class);
		
		assertEquals(3, usuarios.length);
	}
	
	@Test
	void buscarUsuarioPorId() {
		
		ResponseEntity<UsuarioDTO> usuario = restTemplate.getForEntity(baseurl+"/buscarUsuario/"+3, UsuarioDTO.class);
		
		assertNotNull(usuario);
		assertEquals("Juan", usuario.getBody().getNombre());
		
	}
	
	
	
	@Test
	void actualizarUsuario() {
	
	    Long id = 6L;

	    UsuarioDTO usuarioActualizadoDTO = new UsuarioDTO();
	    usuarioActualizadoDTO.setNombre("NuevoNombre");
	    usuarioActualizadoDTO.setApellido("Nuevo Apellido");
	    usuarioActualizadoDTO.setDni("98765430");
	    usuarioActualizadoDTO.setEdad(35);
	    usuarioActualizadoDTO.setGenero("M");
	    usuarioActualizadoDTO.setEmail("nuevoemail0@gmail.com");
	    usuarioActualizadoDTO.setCelular("987654320");

	    restTemplate.put(baseurl+"/actualizarUsuario/"+id, usuarioActualizadoDTO);

	 
	    ResponseEntity<UsuarioDTO> buscarUsuario = restTemplate.getForEntity(baseurl+"/buscarUsuario/"+id, UsuarioDTO.class);

	  
	    assertEquals(HttpStatus.OK, buscarUsuario.getStatusCode());
	    assertNotNull(buscarUsuario.getBody());
	    assertEquals(usuarioActualizadoDTO.getNombre(), buscarUsuario.getBody().getNombre());
	    assertEquals(usuarioActualizadoDTO.getApellido(), buscarUsuario.getBody().getApellido());
	    assertEquals(usuarioActualizadoDTO.getDni(), buscarUsuario.getBody().getDni());
	    assertEquals(usuarioActualizadoDTO.getEdad(), buscarUsuario.getBody().getEdad());
	    assertEquals(usuarioActualizadoDTO.getGenero(), buscarUsuario.getBody().getGenero());
	    assertEquals(usuarioActualizadoDTO.getEmail(), buscarUsuario.getBody().getEmail());
	    assertEquals(usuarioActualizadoDTO.getCelular(), buscarUsuario.getBody().getCelular());

	}
	
	@Test
	void eliminarUsuario() {
		
	    // Crea un nuevo usuario
	    UsuarioDTO usuarioDTO = new UsuarioDTO();
	    usuarioDTO.setNombre("Juan");
	    usuarioDTO.setApellido("Perez");
	    usuarioDTO.setDni("66666666");
	    usuarioDTO.setEdad(30);
	    usuarioDTO.setGenero("M");
	    usuarioDTO.setEmail("juanperez4@gmail.com");
	    usuarioDTO.setCelular("777666555");
	    
	    ResponseEntity<UsuarioDTO> responseCrearUsuario = restTemplate.postForEntity(baseurl+"/crearUsuario", usuarioDTO, UsuarioDTO.class);
	    
	    assertEquals(HttpStatus.CREATED, responseCrearUsuario.getStatusCode());
	    assertNotNull(responseCrearUsuario.getBody());

	    // Obtiene el ID del usuario creado
	    Long usuarioId = responseCrearUsuario.getBody().getId();
	    assertNotNull(usuarioId);

	    // Elimina el usuario creado
	    restTemplate.delete(baseurl + "/eliminarUsuario/" + usuarioId);

	    // Verifica que el usuario ha sido eliminado correctamente
	    UsuarioDTO[] lista = restTemplate.getForObject(baseurl + "/listarUsuarios", UsuarioDTO[].class);
	    
	    assertEquals(5, lista.length);
		
	}
}
