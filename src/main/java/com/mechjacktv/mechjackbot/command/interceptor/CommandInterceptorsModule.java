package com.mechjacktv.mechjackbot.command.interceptor;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.RestrictToRoles;

public final class CommandInterceptorsModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.bindInterceptor(Matchers.subclassesOf(Command.class),
        new CommandHandleMessageMethodMatcher().and(Matchers.annotatedWith(RestrictToRoles.class)),
        new RestrictToRolesMethodInterceptor(this.getProvider(CommandUtils.class)));
    this.bindInterceptor(Matchers.subclassesOf(Command.class),
        new CommandHandleMessageMethodMatcher(),
        new CoolDownMethodInterceptor(this.getProvider(CommandUtils.class)));
    this.bindInterceptor(Matchers.subclassesOf(Command.class),
        new CommandHandleMessageMethodMatcher(),
        new LogCommandHandleMessageMethodInterceptor());
  }

}
