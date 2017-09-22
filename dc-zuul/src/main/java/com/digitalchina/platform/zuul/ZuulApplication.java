package com.digitalchina.platform.zuul;

import com.digitalchina.platform.zuul.filter.DcRouteFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.digitalchina")
@EnableZuulProxy
public class ZuulApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ZuulApplication.class);
    }

    @Bean
    @LoadBalanced
    public RestTemplate retryableLoadbalancedRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public DcRouteFilter dcRouteFilter(){
        return new DcRouteFilter();
    }

	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}
}
