package com.mechjacktv.mechjackbot.command.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.command.CommandUtils;

import javax.inject.Provider;

public class GlobalCoolDownMethodInterceptor implements MethodInterceptor {

    private final Provider<CommandUtils> commandUtils;

    public GlobalCoolDownMethodInterceptor(final Provider<CommandUtils> commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Command thisCommand = (Command) invocation.getThis();

        if (this.commandUtils.get().isGloballyCooledDown(thisCommand)) {
            return invocation.proceed();
        }
        return null;
    }

}
