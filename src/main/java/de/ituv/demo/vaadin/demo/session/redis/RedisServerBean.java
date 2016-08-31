package de.ituv.demo.vaadin.demo.session.redis;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Protocol;
import redis.embedded.RedisServer;

@Configuration
class RedisServerBean implements InitializingBean, DisposableBean, BeanDefinitionRegistryPostProcessor {
    private RedisServer redisServer;
    

    public void afterPropertiesSet() throws Exception {
    	testPort(Protocol.DEFAULT_PORT);
    	
        redisServer = new RedisServer(Protocol.DEFAULT_PORT);
    	
        redisServer.start();
    }

    private void testPort(int port) {
    	try {
    		Socket s = new Socket("localhost", port);
    		throw new IllegalStateException("Port "+port+ " is in Use! Server already started? (kill redis-server process)");
    	} catch(Exception e ){
    		// good
    	}
    	
	}

	public void destroy() throws Exception {
        if(redisServer != null) {
            redisServer.stop();
        }
    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {}

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {}
}
