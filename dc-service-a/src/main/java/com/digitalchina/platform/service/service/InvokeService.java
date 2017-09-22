package com.digitalchina.platform.service.service;

import com.digitalchina.resttemplate.ribbon.retryable.RetryableLoadbalancedRestTemplateUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author liuyd
 * @date 2017/8/16.
 */
@Service
public class InvokeService {
    private static final Logger log = LoggerFactory.getLogger(InvokeService.class);

    @Autowired
    RetryableLoadbalancedRestTemplateUtil restTemplate;
    @Autowired
    @Qualifier("legacyThreadLocalStrategy")
    HystrixConcurrencyStrategy legacyThreadLocalStrategy;

    @HystrixCommand(
            fallbackMethod = "doPlusFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
            }
    )
    public Double doPlus(String service, String contextPath) {
        String uri = String.format("%s/calculator/plus.do?first=%.2f&second=%.2f",
                contextPath, Math.random() * 100, Math.random() * 100);
        return Double.valueOf(restTemplate.get(service,uri,null));
    }

    public Double doPlusFallback(String service, String contextPath) {
        log.info(String.format("doPlus occurred error ,param {service} is [%s],return value [0.001d]", service));
        return 0.001d;
    }
}
