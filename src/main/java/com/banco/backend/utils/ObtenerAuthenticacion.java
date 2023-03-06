package com.banco.backend.utils;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ObtenerAuthenticacion implements Serializable {

	private static final long serialVersionUID = 1L;

	public Authentication getAuthentication() {
	        return SecurityContextHolder.getContext().getAuthentication();
	    }
}
