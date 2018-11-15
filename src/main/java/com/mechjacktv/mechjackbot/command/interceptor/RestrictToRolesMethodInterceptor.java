package com.mechjacktv.mechjackbot.command.interceptor;

import java.lang.reflect.Method;

import javax.inject.Provider;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToRoles;

public class RestrictToRolesMethodInterceptor implements MethodInterceptor {

  private final Provider<CommandUtils> commandUtils;

  RestrictToRolesMethodInterceptor(final Provider<CommandUtils> commandUtils) {
    this.commandUtils = commandUtils;
  }

  @Override
  public final Object invoke(final MethodInvocation invocation) throws Throwable {
    final Method method = invocation.getMethod();
    final RestrictToRoles roles = method.getAnnotation(RestrictToRoles.class);
    final Command thisCommand = (Command) invocation.getThis();
    final MessageEvent messageEvent = (MessageEvent) invocation.getArguments()[0];

    if (this.commandUtils.get().hasRole(thisCommand, messageEvent, roles.value())) {
      return invocation.proceed();
    }
    return null;
  }

}
