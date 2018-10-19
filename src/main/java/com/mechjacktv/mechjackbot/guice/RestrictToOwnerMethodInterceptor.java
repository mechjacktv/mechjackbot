package com.mechjacktv.mechjackbot.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.CommandUtils;

public class RestrictToOwnerMethodInterceptor implements MethodInterceptor {

    private final CommandUtils commandUtils;

    public RestrictToOwnerMethodInterceptor(final CommandUtils commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object thisInstance = invocation.getThis();

        if(Command.class.isAssignableFrom(thisInstance.getClass())) {
            final MessageEvent messageEvent = (MessageEvent) invocation.getArguments()[0];

            if(this.commandUtils.isChannelOwner(messageEvent)) {
                return invocation.proceed();
            }
        }
        throw new IllegalStateException("`@RestrictToOwner` MUST only be placed on implementors of `Command`");
    }

}
