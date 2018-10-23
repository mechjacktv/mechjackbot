package com.mechjacktv.mechjackbot.command.interceptor;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

final class LogCommandHandleMessageMethodInterceptor implements MethodInterceptor {

  private final Map<String, Logger> loggers;

  LogCommandHandleMessageMethodInterceptor() {
    this.loggers = new HashMap<>();
  }

  @Override
  public final Object invoke(final MethodInvocation invocation) {
    final Command thisCommand = (Command) invocation.getThis();
    final MessageEvent messageEvent = (MessageEvent) invocation.getArguments()[0];

    getLogger(thisCommand.getName()).info(
        String.format("Executed: trigger=%s, user=%s, message=%s",
            thisCommand.getTrigger(),
            messageEvent.getChatUser().getUsername(),
            messageEvent.getMessage()));
    try {
      return invocation.proceed();
    } catch (final Throwable t) {
      getLogger(thisCommand.getName()).error(
          String.format("Failed: trigger=%s, user=%s, message=%s",
              thisCommand.getTrigger(),
              messageEvent.getChatUser().getUsername(),
              messageEvent.getMessage()), t);
    }
    return null;
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
