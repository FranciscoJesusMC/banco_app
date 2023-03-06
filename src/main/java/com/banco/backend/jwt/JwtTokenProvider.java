package com.banco.backend.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpiration;
	
	
	public String generarToken(Authentication authentication) {
		String name =authentication.getName();
		Date fechaActual = new Date();
		Date fechaExpiracion = new Date(fechaActual.getTime() + jwtExpiration);
		
		String token = Jwts.builder().setSubject(name).setIssuedAt(new Date()).setExpiration(fechaExpiracion)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		
		return token;
	}
	
	public String obtenerUsernameDeLaSolicitud(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	public boolean validarToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
			
		} catch (SignatureException e) {
		      	log.error("Invalid JWT signature: {}", e.getMessage());
		    } catch (MalformedJwtException e) {
		    	log.error("Invalid JWT token: {}", e.getMessage());
		    } catch (ExpiredJwtException e) {
		    	log.error("JWT token is expired: {}", e.getMessage());
		    } catch (UnsupportedJwtException e) {
		    	log.error("JWT token is unsupported: {}", e.getMessage());
		    } catch (IllegalArgumentException e) {
		    	log.error("JWT claims string is empty: {}", e.getMessage());
		    }
		
			return false;
	}
}
