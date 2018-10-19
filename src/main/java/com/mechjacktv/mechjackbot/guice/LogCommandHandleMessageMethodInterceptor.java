package com.mechjacktv.mechjackbot.guice;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class LogCommandHandleMessageMethodInterceptor implements MethodInterceptor {

    private final Map<String, Logger> loggers;

    public LogCommandHandleMessageMethodInterceptor() {
        this.loggers = new HashMap<>();
    }

    @Override
    public final Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object thisInstance = invocation.getThis();

        if (Command.class.isAssignableFrom(thisInstance.getClass())) {
            final Command thisCommand = (Command) thisInstance;
            final MessageEvent messageEvent = (MessageEvent) invocation.getArguments()[0];

            getLogger(thisCommand.getName()).info(
                    String.format("Command Triggered: user=%s, commandTrigger=%s, message=%s",
                            messageEvent.getChatUser().getUsername(),
                            thisCommand.getTrigger(),
                            messageEvent.getMessage()));
            try {
                return invocation.proceed();
            } catch (final Throwable t) {
                getLogger(thisCommand.getName()).error(
                        String.format("Command Failed: user=%s, commandTrigger=%s, message=%s, errorMessage=%s",
                                messageEvent.getChatUser().getUsername(),
                                thisCommand.getTrigger(),
                                messageEvent.getMessage(),
                                t.getLocalizedMessage()));
                throw t;
            }
        }
        throw new IllegalStateException("This should only be called when executing `Command#handleMessage`");
    }

    private Logger getLogger(final String name) {
        if (this.loggers.containsKey(name)) {
            return this.loggers.get(name);
        }

        final Logger logger = LoggerFactory.getLogger(name);

        this.loggers.put(name, logger);
        return logger;
    }

}
