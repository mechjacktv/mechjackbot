package com.mechjacktv.mechjackbot.command.interceptor;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.CommandUtils;

public class CommandInterceptorsModule extends AbstractModule {

    @Override
    protected void configure() {
        bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher().and(Matchers.annotatedWith(RestrictToOwner.class)),
                new RestrictToOwnerMethodInterceptor(getProvider(CommandUtils.class)));
        bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher().and(Matchers.annotatedWith(RestrictToPrivileged.class)),
                new RestrictToPrivilegedMethodInterceptor(getProvider(CommandUtils.class)));
        bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher().and(Matchers.annotatedWith(RestrictToRegular.class)),
                new RestrictToRegularMethodInterceptor(getProvider(CommandUtils.class)));
        bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher().and(Matchers.annotatedWith(GlobalCoolDown.class)),
                new GlobalCoolDownMethodInterceptor(getProvider(CommandUtils.class)));
        bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher(),
                new LogCommandHandleMessageMethodInterceptor());
    }
}
