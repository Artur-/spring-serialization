package de.ituv.demo.vaadin.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class DummyAuthenticationManager implements AuthenticationManager {

	public static final String ROLE_USER = "USER";
	public static final String ROLE_ADMIN = "ADMIN";
	
	
	public DummyAuthenticationManager() {
		super();
	
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if(authentication == null)
			throw new IllegalArgumentException("Authentication Token cannot be null!");
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
		return token;
	}

}
