package com.mechjacktv.mechjackbot.guice;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogCommandHandleMessageMethodInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LogCommandHandleMessageMethodInterceptor.class);

    @Override
    public final Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object thisInstance = invocation.getThis();

        if (Command.class.isAssignableFrom(thisInstance.getClass())) {
            final Command thisCommand = (Command) thisInstance;
            final MessageEvent messageEvent = (MessageEvent) invocation.getArguments()[0];

            log.info(String.format("Command Triggered: user=%s, commandTrigger=%s, message=%s",
                    messageEvent.getChatUser().getUsername(),
                    thisCommand.getCommandTrigger(),
                    messageEvent.getMessage()));
            try {
                return invocation.proceed();
            } catch (final Throwable t) {
                log.error(String.format("Command Failed: user=%s, commandTrigger=%s, message=%s, errorMessage=%s",
                        messageEvent.getChatUser().getUsername(),
                        thisCommand.getCommandTrigger(),
                        messageEvent.getMessage(),
                        t.getLocalizedMessage()));
                throw t;
            }
        }
        throw new IllegalStateException("This should only be called when executing `Command#handleMessage`");
    }
}
