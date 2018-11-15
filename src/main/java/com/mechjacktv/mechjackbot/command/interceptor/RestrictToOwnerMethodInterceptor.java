package com.mechjacktv.mechjackbot.command.interceptor;

import javax.inject.Provider;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;

final class RestrictToOwnerMethodInterceptor implements MethodInterceptor {

  private final Provider<CommandUtils> commandUtils;

  RestrictToOwnerMethodInterceptor(final Provider<CommandUtils> commandUtils) {
    this.commandUtils = commandUtils;
  }

  @Override
  public final Object invoke(final MethodInvocation invocation) throws Throwable {
    final MessageEvent messageEvent = (MessageEvent) invocation.getArguments()[0];

    if (this.commandUtils.get().isChannelOwner(messageEvent)) {
      return invocation.proceed();
    }
    return null;
  }

}
