package de.ituv.demo.vaadin.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.ws.RequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.vaadin.spring.security.VaadinSecurityContext;

@Component
public class DemoSecurityServletFilter extends GenericFilterBean {
	static final String REMOTE_USERID = "remote.userid"; 
	static final String REMOTE_IPADDR = "remote.ipaddr"; 
	
	@Autowired
    private VaadinSecurityContext vaadinSecurityContext;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String pathinfo = null;
		if(request instanceof FirewalledRequest ){
			pathinfo = ((FirewalledRequest)(request)).getPathInfo();
		}
		
		SecurityContext auth = SecurityContextHolder.getContext();
		
		MDC.put(REMOTE_IPADDR, request.getRemoteAddr());

		if (auth!=null && auth.getAuthentication() != null){
			MDC.put(REMOTE_USERID, auth.getAuthentication().getName());
		}
		
		chain.doFilter(request, response);

		MDC.remove(REMOTE_USERID);
		MDC.remove(REMOTE_IPADDR);
		
		
		

	}
	
}
