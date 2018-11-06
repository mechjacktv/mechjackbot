package com.mechjacktv.mechjackbot.command.interceptor;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.CommandUtils;

public final class DefaultCommandInterceptorsModule extends AbstractModule {

    @Override
    protected final void configure() {
        this.bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher().and(Matchers.annotatedWith(RestrictToOwner.class)),
                new RestrictToOwnerMethodInterceptor(this.getProvider(CommandUtils.class)));
        this.bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher().and(Matchers.annotatedWith(RestrictToPrivileged.class)),
                new RestrictToPrivilegedMethodInterceptor(this.getProvider(CommandUtils.class)));
        this.bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher().and(Matchers.annotatedWith(RestrictToRegular.class)),
                new RestrictToRegularMethodInterceptor(this.getProvider(CommandUtils.class)));
        this.bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher().and(Matchers.annotatedWith(GlobalCoolDown.class)),
                new GlobalCoolDownMethodInterceptor(this.getProvider(CommandUtils.class)));
        this.bindInterceptor(Matchers.subclassesOf(Command.class),
                new CommandHandleMessageMethodMatcher(),
                new LogCommandHandleMessageMethodInterceptor());
    }

}
