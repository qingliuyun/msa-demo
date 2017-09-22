package com.digitalchina.platform.service;

import com.digitalchina.invoketrace.client.InvokeHolder;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

import java.util.concurrent.Callable;

/**
 * @author liuyd
 * Date: 2017/9/7.
 * 将父线程中threadlocal值传入hystrix command子线程
 */
public class LegacyThreadLocalStrategy extends HystrixConcurrencyStrategy {
    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        String routeContext = InvokeHolder.getRouteContext();
        return () -> {
            InvokeHolder.setRouteContext(routeContext);
            return callable.call();
        };
    }
}
