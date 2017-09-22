package com.digitalchina.platform.service;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.digitalchina")
@EnableCircuitBreaker
public class BootApplication extends SpringBootServletInitializer {


	@Bean
	@LoadBalanced
	RestTemplate retryableLoadbalancedRestTemplate(){
		return new RestTemplate();
	}

	@Bean
	/**
	 * 注入一个自定义的Hystrix策略
	 */
	HystrixConcurrencyStrategy legacyThreadLocalStrategy(){
		HystrixConcurrencyStrategy strategy = new LegacyThreadLocalStrategy();
		HystrixPlugins.getInstance().registerConcurrencyStrategy(
				strategy
		);
		return strategy;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BootApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}
