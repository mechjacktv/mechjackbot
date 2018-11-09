package com.mechjacktv.mechjackbot.command.interceptor;

import java.lang.reflect.Method;

import com.google.inject.matcher.AbstractMatcher;

final class CommandHandleMessageMethodMatcher extends AbstractMatcher<Method> {

  @Override
  public final boolean matches(final Method method) {
    return "handleMessage".equals(method.getName());
  }

}
