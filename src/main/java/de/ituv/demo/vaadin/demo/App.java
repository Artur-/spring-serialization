package de.ituv.demo.vaadin.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.vaadin.spring.security.annotation.EnableVaadinSecurity;

import com.vaadin.spring.boot.annotation.EnableVaadinServlet;

@Configuration
@EnableVaadinSecurity
@EnableAutoConfiguration
@ComponentScan(basePackages={"de.ituv.demo"})
@PropertySource(value="classpath:application.properties")
@EnableVaadinServlet
@EnableRedisHttpSession
public class App {
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
}
