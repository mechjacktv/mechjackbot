package com.mechjacktv.mechjackbot.command.interceptor;

import javax.inject.Provider;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;

final class CoolDownMethodInterceptor implements MethodInterceptor {

  private final Provider<CommandUtils> commandUtils;

  CoolDownMethodInterceptor(final Provider<CommandUtils> commandUtils) {
    this.commandUtils = commandUtils;
  }

  @Override
  public final Object invoke(final MethodInvocation invocation) throws Throwable {
    final Command thisCommand = (Command) invocation.getThis();
    final MessageEvent messageEvent = (MessageEvent) invocation.getArguments()[0];

    if (this.commandUtils.get().isCooledDown(thisCommand, messageEvent)) {
      return invocation.proceed();
    }
    return null;
  }

}
