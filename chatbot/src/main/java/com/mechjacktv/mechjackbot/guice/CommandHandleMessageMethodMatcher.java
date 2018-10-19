package com.mechjacktv.mechjackbot.guice;

import com.google.inject.matcher.AbstractMatcher;

import java.lang.reflect.Method;

public final class CommandHandleMessageMethodMatcher extends AbstractMatcher<Method> {

    @Override
    public final boolean matches(final Method method) {
        return "handleMessage".equals(method.getName());
    }
}
