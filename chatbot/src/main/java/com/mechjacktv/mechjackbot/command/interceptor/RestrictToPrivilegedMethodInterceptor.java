package com.mechjacktv.mechjackbot.command.interceptor;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.CommandUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Provider;

public class RestrictToPrivilegedMethodInterceptor implements MethodInterceptor {

    private final Provider<CommandUtils> commandUtils;

    public RestrictToPrivilegedMethodInterceptor(final Provider<CommandUtils> commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final MessageEvent messageEvent = (MessageEvent) invocation.getArguments()[0];

        if (this.commandUtils.get().isPrivilegedViewer(messageEvent)) {
            return invocation.proceed();
        }
        return null;
    }

}
