package de.ituv.demo.vaadin.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.vaadin.spring.security.GenericVaadinSecurity;
import org.vaadin.spring.security.VaadinSecurityContext;
import org.vaadin.spring.security.web.VaadinDefaultRedirectStrategy;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;
import org.vaadin.spring.security.web.authentication.SavedRequestAwareVaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class DemoSecurityConfiguration  {

	 @Autowired
	private DemoSecurityServletFilter mogSecFilter;
    
	public DemoSecurityConfiguration() {
		super();
	}
	
	@Autowired
    public void registerSharedAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
        	.inMemoryAuthentication()
        	.withUser("NeverForgetMe").password("").roles("USER");
    }
	
	@Bean
	public FilterRegistrationBean userInsertingMdcFilterRegistrationBean() {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    registrationBean.setFilter(mogSecFilter);
	    registrationBean.setOrder(Integer.MAX_VALUE);
	    return registrationBean;
	}
	
	
	@Bean
	public FilterRegistrationBean securityFilterChain(@Qualifier(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME) Filter securityFilter) {
	    FilterRegistrationBean registration = new FilterRegistrationBean(securityFilter);
	    registration.setOrder(Integer.MAX_VALUE - 1);
	    registration.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
	    return registration;
	}

	@Bean(name = "authenticationManager")
	protected AuthenticationManager authenticationManager() throws Exception {
		return new DummyAuthenticationManager();
	}
	
    @Configuration
    @Order(1)
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {

    	public WebSecurityConfig() {
			super();
		}

		@Autowired
        private VaadinSecurityContext vaadinSecurityContext;

        @Autowired
        private ApplicationContext ctx;
        
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
            .withUser("NeverForgetMe").password("").roles("USER");
        }
        
        public void afterPropertiesSet() throws Exception {
            this.vaadinSecurityContext.addAuthenticationSuccessHandler(redirectSaveHandler());
            if(vaadinSecurityContext instanceof GenericVaadinSecurity) {
            	((GenericVaadinSecurity)vaadinSecurityContext).setLogoutProcessingUrl("/logout");
            }
            
        }
        
        @Bean
        public RequestCache requestCache() {
            RequestCache requestCache = new HttpSessionRequestCache();
            return requestCache;
        }

        @Bean
        public RequestCacheAwareFilter requestCacheAwareFilter() {
            RequestCacheAwareFilter filter = new RequestCacheAwareFilter(requestCache());
            return filter;
        }

        @Bean
        public VaadinRedirectStrategy vaadinRedirectStrategy() {
            return new VaadinDefaultRedirectStrategy();
        }

        @Bean
        public VaadinAuthenticationSuccessHandler redirectSaveHandler() {
            SavedRequestAwareVaadinAuthenticationSuccessHandler handler = new SavedRequestAwareVaadinAuthenticationSuccessHandler();

            handler.setRedirectStrategy(vaadinRedirectStrategy());
            handler.setRequestCache(requestCache());
            handler.setDefaultTargetUrl("/ui/demo");
            handler.setTargetUrlParameter("r");

            return handler;
        }
        

		protected void configure(HttpSecurity http) throws Exception {
			http .requestMatchers()
            .antMatchers("/**")
            .and()
            	.authorizeRequests()
        		.antMatchers("/login").permitAll()
            		.antMatchers("/ui/**").authenticated()
            		.anyRequest().permitAll()
            	
            .and()
                .sessionManagement()
                    .sessionFixation()
                        .migrateSession()
            .and()
                .csrf().disable().
                	headers()
	                    	.frameOptions().disable()
	        .and()
	        	.exceptionHandling()
                	.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            .and()
            	.logout()
            		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            			.invalidateHttpSession(true)
            				.logoutSuccessUrl("/login")
            					.permitAll();
		}

		public void configure(WebSecurity web) throws Exception {
			 web
             .ignoring()
                 .antMatchers(
                 		"/css", "/images", "/img", "/VAADIN/**", "/HEARTBEAT/**", "/vaadinServlet/VAADIN/**",  "/vaadinServlet/HEARTBEAT/**",
                 		"/ui/VAADIN/**", "/ui/HEARTBEAT/**");
		
		}

    }
    
}
