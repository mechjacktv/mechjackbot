package com.mechjacktv.mechjackbot.command.interceptor;

import java.lang.reflect.Method;

import com.google.inject.matcher.AbstractMatcher;

final class CommandHandleMessageMethodMatcher extends AbstractMatcher<Method> {

  public static final String MATCHING_METHOD_NAME = "handleMessageEvent";

  @Override
  public final boolean matches(final Method method) {
    return MATCHING_METHOD_NAME.equals(method.getName());
  }

}
