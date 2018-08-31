package com.mechjacktv.mechjackbot.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CooldownInterceptor implements MethodInterceptor {

    private static final long COOLDOWN_PERIOD = 5000;

    private final Map<Method, Long> lastCalls = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        System.out.println("I'm being cooled down.");

        final Long now = System.currentTimeMillis();
        final Long lastCalled = lastCalls.get(invocation.getMethod());

        if(lastCalled == null || now - lastCalled > COOLDOWN_PERIOD) {
            lastCalls.put(invocation.getMethod(), now);
            return invocation.proceed();
        }
        return null;
    }

}
