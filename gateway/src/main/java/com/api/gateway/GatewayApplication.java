package com.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.api.gateway.configs.EventLoopNettyCustomizer;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = { "com.ekhata.api.gateway.repositories" })
@ComponentScan("com.api")
public class GatewayApplication {

	public static void main(final String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
		final NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
		webServerFactory.addServerCustomizers(new EventLoopNettyCustomizer());
		return webServerFactory;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
