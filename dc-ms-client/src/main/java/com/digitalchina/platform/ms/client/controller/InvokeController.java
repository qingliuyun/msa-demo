package com.digitalchina.platform.ms.client.controller;

import com.digitalchina.resttemplate.ribbon.retryable.RetryableLoadbalancedRestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuydj  created at 2017/9/21.
 */
@RestController
@Configuration
public class InvokeController {
    @Autowired
    RetryableLoadbalancedRestTemplateUtil restTemplate;

    @Value("${invoke.zuulHost}")
    String zuulHost;
    @Value("${invoke.invokeUri}")
    String invokeUri;
    @Value("${invoke.queryString}")
    String queryString;

    @GetMapping(value = "/invoke")
    public Object invoke(){
        return restTemplate.get(zuulHost,String.format("%s?%s",invokeUri,queryString),null);
    }
}
