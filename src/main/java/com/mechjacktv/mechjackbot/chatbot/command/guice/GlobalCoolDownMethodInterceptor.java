package com.mechjacktv.mechjackbot.chatbot.command.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.chatbot.command.CommandUtils;

public class GlobalCoolDownMethodInterceptor implements MethodInterceptor {

    private final CommandUtils commandUtils;

    public GlobalCoolDownMethodInterceptor(final CommandUtils commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object thisInstance = invocation.getThis();

        if (Command.class.isAssignableFrom(thisInstance.getClass())) {
            final Command thisCommand = (Command) thisInstance;

            if (this.commandUtils.isGloballyCooledDown(thisCommand)) {
                return invocation.proceed();
            }
        }
        throw new IllegalStateException("`@GlobalCoolDown` MUST only be placed on implementors of `Command`");
    }

}
