package com.digitalchina.platform.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient //添加发现服务的能力
@SpringBootApplication
public class BootApplication extends SpringBootServletInitializer {


//	@Bean
//	@LoadBalanced //开启负载均衡
//	RestTemplate restTemplate(){
//		return new RestTemplate();
//	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BootApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}
