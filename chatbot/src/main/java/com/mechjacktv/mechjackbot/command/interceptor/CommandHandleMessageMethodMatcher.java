package com.mechjacktv.mechjackbot.command.interceptor;

import com.google.inject.matcher.AbstractMatcher;

import java.lang.reflect.Method;

final class CommandHandleMessageMethodMatcher extends AbstractMatcher<Method> {

  @Override
  public final boolean matches(final Method method) {
    return "handleMessage".equals(method.getName());
  }

}
