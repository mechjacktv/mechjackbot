package com.mechjacktv.mechjackbot.chatbot.command.log;

import com.google.inject.matcher.AbstractMatcher;

import java.lang.reflect.Method;

public final class LogCommandHandleMessageMethodMatcher extends AbstractMatcher<Method> {

    @Override
    public final boolean matches(final Method method) {
        return "handleMessage".equals(method.getName());
    }
}
