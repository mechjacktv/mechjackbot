package com.mechjacktv.mechjackbot.command.interceptor;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.inject.Provider;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.mechjacktv.mechjackbot.*;

final class CoolDownMethodInterceptor implements MethodInterceptor {

  private final Provider<CommandUtils> commandUtils;

  CoolDownMethodInterceptor(final Provider<CommandUtils> commandUtils) {
    this.commandUtils = commandUtils;
  }

  @Override
  public final Object invoke(final MethodInvocation invocation) throws Throwable {
    final Method method = invocation.getMethod();
    final NoCoolDown noCoolDown = method.getAnnotation(NoCoolDown.class);
    final Command thisCommand = (Command) invocation.getThis();
    final MessageEvent messageEvent = (MessageEvent) invocation.getArguments()[0];

    if (Objects.nonNull(noCoolDown) || this.commandUtils.get().isCooledDown(thisCommand, messageEvent)) {
      return invocation.proceed();
    }
    return null;
  }

}
